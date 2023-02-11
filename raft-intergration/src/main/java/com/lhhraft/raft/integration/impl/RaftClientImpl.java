package com.lhhraft.raft.integration.impl;

import com.lhhraft.raft.facade.model.AppendEntriesRequest;
import com.lhhraft.raft.facade.model.AppendEntriesResponse;
import com.lhhraft.raft.facade.model.VoteRequest;
import com.lhhraft.raft.facade.model.VoteResponse;
import com.lhhraft.raft.integration.RaftClient;
import com.lhhraft.raft.integration.consumer.DubboServiceConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: raft 发起请求
 */
@Slf4j
@Service
public class RaftClientImpl implements RaftClient {
    @Resource
    private DubboServiceConsumer dubboServiceConsumer;

    @Override
    public VoteResponse requestVote(Long remoteServerId,
                                    VoteRequest voteRequest) {
        try {
            return dubboServiceConsumer
                    .getFacade(remoteServerId)
                    .requestVote(voteRequest);
        } catch (Throwable t) {
            log.error("投票失败", t);
            return new VoteResponse(0L, Boolean.FALSE);
        }
    }

    @Override
    public AppendEntriesResponse appendEntries(Long remoteServerId,
                                               AppendEntriesRequest appendEntriesRequest) {
        try {
            return dubboServiceConsumer
                    .getFacade(remoteServerId)
                    .appendEntries(appendEntriesRequest);
        } catch (Throwable t) {
            log.error("附加日志失败", t);
            return new AppendEntriesResponse(0L, Boolean.FALSE);
        }
    }
}
