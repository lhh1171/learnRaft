package com.lhhraft.raft.api;


import com.lhhraft.raft.model.AppendEntriesRequest;
import com.lhhraft.raft.model.AppendEntriesResponse;
import com.lhhraft.raft.model.VoteRequest;
import com.lhhraft.raft.model.VoteResponse;

/**
 * @description: raft 门面：接受其他server的请求
 */
public interface RaftFacade {

    /**
     * 接受请求：投票
     *
     * @param voteRequest 请求参数
     * @return 结果
     */
    VoteResponse requestVote(VoteRequest voteRequest);

    /**
     * 接受请求：附加日志
     *
     * @param appendEntriesRequest 请求
     * @return 结果
     */
    AppendEntriesResponse appendEntries(AppendEntriesRequest appendEntriesRequest);
}
