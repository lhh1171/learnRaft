package com.lhhraft.core.service.handler;

/**
 * @description: 状态机控制器

 */
public interface StateMachineHandler {
    /**
     * commit到apply状态
     */
    void commit2Apply();
}
