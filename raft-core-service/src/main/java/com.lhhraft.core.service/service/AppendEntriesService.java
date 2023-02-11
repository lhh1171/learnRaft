package com.lhhraft.core.service.service;


import com.lhhraft.raft.model.AppendEntriesRequest;
import com.lhhraft.raft.model.AppendEntriesResponse;

/**
 * @description: 附件日志条目服务
 */
public interface AppendEntriesService {

    /**
     * 发起请求：附加日志
     *
     * @param appendEntriesRequest 请求
     * @return 结果
     */
    AppendEntriesResponse receiveAppendEntries(AppendEntriesRequest appendEntriesRequest);
}
