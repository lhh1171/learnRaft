package com.lhhraft.core.service.component;


import com.lhhraft.raft.model.VoteRequest;

/**
 * @description: 发起投票服务
 */
public interface VoteComponent {
    /**
     * 广播投票
     * 备注：同步获取投票结果
     *
     * @return 投票结果
     */
    Boolean broadcastVote();

    /**
     * 发起投票
     *
     * @param remoteServerId 远程服务的server id
     * @param voteRequest    请求
     * @return 投票结果
     */
    Boolean requestVote(Long remoteServerId, VoteRequest voteRequest);
}
