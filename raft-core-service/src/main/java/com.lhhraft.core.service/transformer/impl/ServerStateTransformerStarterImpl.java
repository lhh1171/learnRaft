package com.lhhraft.core.service.transformer.impl;

import com.lhhraft.core.service.transformer.ServerStateTransformer;
import com.lhhraft.core.service.transformer.ServerStateTransformerStarter;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

/**
 * @description: 节点状态转换器启动（即角色转换状态机）
 */
@Service
public class ServerStateTransformerStarterImpl implements ServerStateTransformerStarter {

    @Resource(name = "followerStateImpl")
    private ServerStateTransformer serverStateTransformer;

    @Override
    public void start() {
        // follower 为入口
        serverStateTransformer.execute();
    }
}
