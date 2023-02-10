package com.lhhraft.core.service.service;


import com.lhhraft.core.config.loader.model.VoteRequest;
import com.lhhraft.core.config.loader.model.VoteResponse;

/**
 * @description: 接受投票服务
 */
public interface VoteService {

    /**
     * 发起投票
     *
     * @param voteRequest 请求
     * @return 投票结果
     */
    VoteResponse receiveVote(VoteRequest voteRequest);
}
