package com.lhhraft.core.service.transformer.impl;

import com.google.common.collect.Lists;
import com.lhhraft.raft.facade.ConfigLoader;
import com.lhhraft.raft.facade.enums.ServerStateEnum;
import com.lhhraft.raft.facade.model.PersistentStateModel;
import com.lhhraft.raft.facade.model.RaftCoreModel;
import com.lhhraft.core.service.component.VoteComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * @description: candidate状态
 */
@Slf4j
@Service(value = "candidateStateImpl")
public class CandidateStateImpl extends AbstractServerStateTransformer {

    @Resource
    private VoteComponent voteComponent;

    @Override
    public void execute() {
        log.info("当前为 Candidate：" + RaftCoreModel.getSingleton());
        Lock lock = RaftCoreModel.getLock();
        lock.lock();
        try {
            RaftCoreModel coreModel = RaftCoreModel.getSingleton();
            PersistentStateModel persistentState = coreModel.getPersistentState();

            //1.给自己投票:投一票、term+1
            persistentState.setVotedFor(ConfigLoader.load().getCurrentServerId());
            persistentState.setCurrentTerm(persistentState.getCurrentTerm() + 1);
            coreModel.setVoteCount(1L);

            //2.candidate发起投票(广播)：使用CountDownLatch实现
            Boolean voteResult = voteComponent.broadcastVote();

            //3.根据投票结果进行设置
            if (voteResult) {
                coreModel.setServerStateEnum(ServerStateEnum.LEADER);
            }
        } finally {
            lock.unlock();
        }

        //4.执行后续节点
        executeNext();

    }

    @Override
    public void preDo() {
        // do nothing
    }

    @Override
    public List<ServerStateEnum> getNextStates() {
        return Lists.newArrayList(
                ServerStateEnum.CANDIDATE,
                ServerStateEnum.LEADER,
                ServerStateEnum.FOLLOWER
        );
    }

    @Override
    public ServerStateEnum getCurrentState() {
        return ServerStateEnum.CANDIDATE;
    }
}
