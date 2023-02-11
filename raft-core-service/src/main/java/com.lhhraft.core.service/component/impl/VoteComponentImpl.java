package com.lhhraft.core.service.component.impl;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.lhhraft.raft.facade.ConfigLoader;
import com.lhhraft.raft.facade.config.ConfigModel;
import com.lhhraft.raft.facade.config.RaftNodeModel;
import com.lhhraft.raft.facade.enums.ServerStateEnum;
import com.lhhraft.raft.facade.exception.ErrorCodeEnum;
import com.lhhraft.raft.facade.exception.RaftException;
import com.lhhraft.raft.facade.model.*;
import com.lhhraft.raft.facade.util.CommonUtil;
import com.lhhraft.core.service.component.VoteComponent;
import com.lhhraft.core.service.pool.RaftThreadPool;
import com.lhhraft.core.service.transformer.convertor.FollowerConvertor;
import com.lhhraft.raft.integration.RaftClient;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @description: 投票

 */
@Slf4j
@Component
public class VoteComponentImpl implements VoteComponent {
    @Resource
    private RaftClient raftClient;


    @Override
    public Boolean broadcastVote() {
        Lock lock = RaftCoreModel.getLock();
        VoteRequest voteRequest = new VoteRequest();
        ConfigModel configModel = ConfigLoader.load();
        lock.lock();
        try {
            //0.数据准备
            RaftCoreModel coreModel = RaftCoreModel.getSingleton();
            PersistentStateModel persistentState = coreModel.getPersistentState();
            LogEntryModel lastLogEntry = persistentState.getLastEntry();

            //1. 组装入参
            voteRequest.setTerm(persistentState.getCurrentTerm());
            voteRequest.setCandidateId(configModel.getCurrentServerId());
            voteRequest.setLastLogIndex(lastLogEntry.getIndex());
            voteRequest.setLastLogTerm(lastLogEntry.getTerm());
        } finally {
            lock.unlock();
        }

        //2.线程池发起请求
        // return multiThreadMode(voteRequest, configModel);
        return singleThreadMode(voteRequest, configModel);
    }

    /**
     * 多线程模式执行
     */
    private Boolean multiThreadMode(VoteRequest voteRequest, ConfigModel configModel) {
        //1.一半以上节点成功，通过countDownLatch来获取
        CountDownLatch countDownLatch = new CountDownLatch(CommonUtil.getMostCount(ConfigLoader.getServerCount()));
        for (RaftNodeModel remoteNode : configModel.getRemoteNodes()) {

            //2. 请求一台服务
            ListenableFuture<Boolean> listenableFuture = RaftThreadPool
                    .execute(() -> requestVote(remoteNode.getServerId(), voteRequest)
                    );

            //3.增加回调方法。
            //noinspection UnstableApiUsage
            Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
                @Override
                public void onSuccess(@NullableDecl Boolean result) {
                    //如果执行成功则减一
                    if (result != null && result) {
                        countDownLatch.countDown();
                    }
                }

                @Override
                public void onFailure(@SuppressWarnings("NullableProblems") Throwable throwable) {
                    log.warn("投票异常", throwable);
                }
                // MoreExecutors.directExecutor()返回guava默认的Executor
            }, MoreExecutors.directExecutor());
        }

        //4.等待1s，获取执行结果。全部执行完成(一半以上server)，则为true
        try {
            return countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RaftException(ErrorCodeEnum.SYSTEM_ERROR, "countDownLatch await error");
        }
    }

    /**
     * 单线程模式执行：方便调试
     */
    private Boolean singleThreadMode(VoteRequest voteRequest, ConfigModel configModel) {
        int successCount = 1;
        for (RaftNodeModel remoteNode : configModel.getRemoteNodes()) {
            Boolean response = requestVote(remoteNode.getServerId(), voteRequest);
            if (response) {
                successCount++;
            }
            if (successCount >= CommonUtil.getMostCount(ConfigLoader.getServerCount())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean requestVote(Long remoteServerId, VoteRequest voteRequest) {
        RaftCoreModel coreModel = RaftCoreModel.getSingleton();
        PersistentStateModel persistentState = coreModel.getPersistentState();
        Long currentTerm = persistentState.getCurrentTerm();

        //1.发起请求
        VoteResponse response = raftClient.requestVote(remoteServerId, voteRequest);

        //2.状态发生变化或者term发生变化，则不作处理。（发送请求的过程过了一段时间，所以需要重新判断一下）
        if (coreModel.getServerStateEnum() != ServerStateEnum.CANDIDATE
                || !voteRequest.getTerm().equals(currentTerm)) {
            return Boolean.FALSE;
        }

        //3.如果response的term大于currentTerm，则转换为follower
        if (response.getTerm() > currentTerm) {
            FollowerConvertor.convert2Follower(response.getTerm(), coreModel);
            return Boolean.FALSE;
        }
        //4.返回投票结果
        else {
            return response.getVoteGranted();
        }
    }
}
