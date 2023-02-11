package com.lhhraft.raft.integration.consumer;


import com.lhhraft.raft.api.RaftFacade;

/**
 * @description: dubbo 服务消费
 */
public interface DubboServiceConsumer {

    /**
     * 通过server id获取对应facade
     *
     * @param serverId id
     * @return facade
     */
    RaftFacade getFacade(Long serverId);
}
