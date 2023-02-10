package com.lhhraft.core.service.transformer;

/**
 * @description: 节点状态转换器启动（即角色转换状态机）

 */
public interface ServerStateTransformerStarter {
    /**
     * 开始执行
     * 启动后，在满足相应条件后，会自动在各个状态之间进行转换
     */
    void start();
}
