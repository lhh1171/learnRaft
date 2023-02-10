package com.lhhraft.core.config.loader.api;


import com.lhhraft.core.config.loader.model.SubmitResponse;

public interface OperationFacade {
    /**
     * 客户端提交数据请求
     *
     * @param option 操作类型
     * @param data   数据
     * @return 提交结果
     */
    SubmitResponse submitData(String option, String data);
}
