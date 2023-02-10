package com.lhhraft.raft.integration;


import com.lhhraft.core.config.loader.model.AppendEntriesRequest;
import com.lhhraft.core.config.loader.model.AppendEntriesResponse;
import com.lhhraft.core.config.loader.model.VoteRequest;
import com.lhhraft.core.config.loader.model.VoteResponse;

/**
 * @description: raft 发起请求
 */
public interface RaftClient {

    /**
     * 发起请求：投票
     *
     * @param remoteServerId 远程服务的server id
     * @param voteRequest    请求参数
     * @return 结果
     */
    VoteResponse requestVote(Long remoteServerId,
                             VoteRequest voteRequest);

    /**
     * 发起请求：附加日志
     *
     * @param remoteServerId       远程服务的server id
     * @param appendEntriesRequest 请求
     * @return 结果
     */
    AppendEntriesResponse appendEntries(Long remoteServerId,
                                        AppendEntriesRequest appendEntriesRequest);
}
