package com.lhhraft.core.service.service;


import com.lhhraft.core.config.loader.model.AppendEntriesRequest;
import com.lhhraft.core.config.loader.model.AppendEntriesResponse;

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
