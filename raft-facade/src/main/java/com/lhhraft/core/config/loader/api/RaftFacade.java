package com.lhhraft.core.config.loader.api;


import com.lhhraft.core.config.loader.model.AppendEntriesRequest;
import com.lhhraft.core.config.loader.model.AppendEntriesResponse;
import com.lhhraft.core.config.loader.model.VoteRequest;
import com.lhhraft.core.config.loader.model.VoteResponse;


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
