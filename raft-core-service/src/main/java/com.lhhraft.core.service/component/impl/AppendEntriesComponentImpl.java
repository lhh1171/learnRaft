package com.lhhraft.core.service.component.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.lhhraft.raft.ConfigLoader;
import com.lhhraft.raft.config.ConfigModel;
import com.lhhraft.raft.config.RaftNodeModel;
import com.lhhraft.raft.constant.CommonConstant;
import com.lhhraft.raft.enums.ServerStateEnum;
import com.lhhraft.raft.model.*;
import com.lhhraft.raft.util.CommonUtil;
import com.lhhraft.core.service.component.AppendEntriesComponent;
import com.lhhraft.core.service.pool.RaftThreadPool;
import com.lhhraft.core.service.transformer.convertor.FollowerConvertor;
import com.lhhraft.raft.integration.RaftClient;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * @description: 附件日志条目服务
 */
@Component
public class AppendEntriesComponentImpl implements AppendEntriesComponent {

    @Resource
    private RaftClient raftClient;

    @Override
    public void broadcastAppendEntries() {
        ConfigModel configModel = ConfigLoader.load();
        Long leaderId = configModel.getLocalNode().getServerId();

        Lock lock = RaftCoreModel.getLock();
        lock.lock();
        try {
            //0.数据准备
            RaftCoreModel coreModel = RaftCoreModel.getSingleton();
            PersistentStateModel persistentState = coreModel.getPersistentState();
            Long currentTerm = persistentState.getCurrentTerm();
            ServerStateModel serverState = coreModel.getServerState();
            Long commitIndex = serverState.getCommitIndex();
            Long lastApplied = serverState.getLastApplied();
            Map<Long, Long> matchIndex = coreModel.getLeaderState().getMatchIndex();

            //1.找到N
            Long indexMaxN = getMaxN(persistentState, commitIndex,
                    lastApplied, matchIndex, configModel);
            if (indexMaxN >= commitIndex) {
                //1.1 重置commitIndex
                serverState.setCommitIndex(indexMaxN);
                //1.2 通知 将新的index  apply 到状态机
                coreModel.getCommitChannel().offer(CommonConstant.CHANNEL_FLAG);
            }

            //2.针对followers： 组装入参 --> 多线程发起请求
            for (RaftNodeModel remoteNode : configModel.getRemoteNodes()) {
                //2.1 组装请求入参
                AppendEntriesRequest request = new AppendEntriesRequest();
                request.setTerm(currentTerm);
                request.setLeaderId(leaderId);
                request.setPreLogIndex(persistentState.getPreEntry().getIndex());
                request.setPreLogTerm(persistentState.getPreEntry().getTerm());
                request.setLeaderCommit(commitIndex);
                //复制leader的log entry
                request.setLogEntries(getNextEntries(remoteNode));

                //2.2 线程池 异步发起单个请求
                RaftThreadPool.execute(() -> requestAppendEntries(remoteNode.getServerId(), request));
            }

        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取下一批 日志条目
     */
    private List<LogEntryModel> getNextEntries(RaftNodeModel remoteNode) {
        //1.数据准备
        RaftCoreModel coreModel = RaftCoreModel.getSingleton();
        LeaderStateModel leaderState = coreModel.getLeaderState();
        PersistentStateModel persistentState = coreModel.getPersistentState();
        List<LogEntryModel> logEntries = persistentState.getLogEntries();

        //2.获取开始和结束索引
        int nextIndex = Math.toIntExact(leaderState.getNextIndex().get(remoteNode.getServerId()));
        int lastLogIndex = Math.toIntExact(persistentState.getLastEntry().getIndex());
        //3.索引不符合预期(已经大于等于最大日志)时，发心跳空包
        if (nextIndex > lastLogIndex) {
            return Lists.newArrayList();
        }
        return logEntries.subList(nextIndex, lastLogIndex + 1);
    }


    @Override
    public void requestAppendEntries(Long serverId, AppendEntriesRequest request) {
        //1.发起RPC请求
        AppendEntriesResponse response = raftClient.appendEntries(serverId, request);

        Lock lock = RaftCoreModel.getLock();
        lock.lock();
        try {
            //0.数据准备
            RaftCoreModel coreModel = RaftCoreModel.getSingleton();
            PersistentStateModel persistentState = coreModel.getPersistentState();
            LogEntryModel lastEntry = persistentState.getLastEntry();
            Long currentTerm = persistentState.getCurrentTerm();
            Map<Long, Long> matchIndex = coreModel.getLeaderState().getMatchIndex();

            //2. 当前节点被废黜，或任期号变更了,不对回复值做处理
            if (coreModel.getServerStateEnum() != ServerStateEnum.LEADER
                    || !currentTerm.equals(request.getTerm())) {
                return;
            }

            //3.Follower发送了更新的任期号，则leader将自己降为Follower
            if (response.getTerm() > currentTerm) {
                FollowerConvertor.convert2Follower(response.getTerm(), coreModel);
            }

            //4. 判断结果，为true: 更新nextIndex和matchIndex
            //更新逻辑：nextIndex为最后一条日志+1，matchIndex为nextIndex-1
            Map<Long, Long> nextIndex = coreModel.getLeaderState().getNextIndex();
            List<LogEntryModel> requestLogs = request.getLogEntries();
            if (response.getSuccess() && CollectionUtil.isNotEmpty(requestLogs)) {
                long next = requestLogs.get(requestLogs.size() - 1).getIndex() + 1;
                nextIndex.put(serverId, next);
                matchIndex.put(serverId, nextIndex.get(serverId) - 1);
            }
            // 为false: nextIndex减一
            if (!response.getSuccess() && nextIndex.get(serverId) > 1) {
                nextIndex.put(serverId, nextIndex.get(serverId) - 1);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 找到N
     * 1.如果存在一个满足N > commitIndex的 N，并且大多数(一半以上)的 matchIndex[i] ≥ N成立，
     * 并且log[N].term == currentTerm成立， 那么令 commitIndex 等于这个 N(论文 5.3 和 5.4 节)。
     * 比如，图6中，令leader commitIndex=5，那么找到的N=7
     * 前者是一半节点匹配后才能提交；后者[log.get(i - baseIndex).getLogTerm() == currentTerm]是防止非本此term的日志覆盖(5.4.2) 比如，在图8(c)中，S1在term=4时为leader，此时，虽然已经复制了index=2的一半以上节点，但是该term=2，非当前term，不能用来提交。
     */
    private Long getMaxN(PersistentStateModel persistentState, Long commitIndex,
                         Long lastApplied, Map<Long, Long> matchIndex,
                         ConfigModel configModel) {
        //1.数据准备
        Long indexMaxN = commitIndex;
        Long currentTerm = persistentState.getCurrentTerm();
        List<LogEntryModel> logEntries = persistentState.getLogEntries();

        //2.找到N
        for (long indexN = commitIndex + 1; indexN <= lastApplied; indexN++) {
            //当前节点已存在，所以初始值为1
            int matchServerCount = 1;
            for (RaftNodeModel remoteNode : configModel.getRemoteNodes()) {
                //2.1 matchIndex[i] ≥ N成立，则加一
                if (matchIndex.get(remoteNode.getServerId()) >= indexN) {
                    matchServerCount++;
                }
            }
            //2.2 判断是否一半以上成立, 并且log[N].term == currentTerm成立
            if (matchServerCount >= CommonUtil.getMostCount(ConfigLoader.getServerCount())
                    && logEntries.get((int) indexN).getTerm().equals(currentTerm)) {
                indexMaxN = indexN;
            }
        }
        return indexMaxN;
    }
}
