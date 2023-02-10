package com.lhhraft.core.service.transformer;


import com.lhhraft.core.config.loader.enums.ServerStateEnum;

/**
 * @description: 状态执行器工厂
 */
public interface ServerStateFactory {
    /**
     * 根据状态获取执行器
     *
     * @param currentState 状态
     * @return 执行器
     */
    ServerStateTransformer getByType(ServerStateEnum currentState);
}
