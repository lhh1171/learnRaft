package com.lhhraft.core.service.transformer.impl;



import com.lhhraft.raft.enums.ServerStateEnum;
import com.lhhraft.core.service.transformer.ServerStateFactory;
import com.lhhraft.core.service.transformer.ServerStateTransformer;

import javax.annotation.Resource;

/**
 * @description: 抽象类

 */
public abstract class AbstractServerStateTransformer implements ServerStateTransformer {
    @Resource
    private ServerStateFactory serverStateFactory;

    /**
     * 依次获取下一个状态，如果满足前置校验，则进入下一个状态
     */
    public void executeNext() {
        for (ServerStateEnum nextState : getNextStates()) {
            ServerStateTransformer nextTransformer = serverStateFactory.getByType(nextState);
            //如果满足前置校验，则进入下一个状态
            if (nextTransformer.preCheck()) {
                //进入状态前的准备工作
                nextTransformer.preDo();
                //执行下一状态
                nextTransformer.execute();
                //只执行第一个匹配到的，理论上会在下一个状态实现中进行后续的跳转，后续state不会再执行；此处break只做标识使用
                break;
            }
        }
    }
}
