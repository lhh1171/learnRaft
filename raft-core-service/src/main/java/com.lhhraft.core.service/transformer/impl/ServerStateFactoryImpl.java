package com.lhhraft.core.service.transformer.impl;

import com.lhhraft.core.config.loader.enums.ServerStateEnum;
import com.lhhraft.core.service.transformer.ServerStateFactory;
import com.lhhraft.core.service.transformer.ServerStateTransformer;
import lombok.Setter;


import java.util.Map;

/**
 * @description: 状态执行器 工厂
 */
public class ServerStateFactoryImpl implements ServerStateFactory {
    @Setter
    private Map<ServerStateEnum, ServerStateTransformer> stateMap;

    @Override
    public ServerStateTransformer getByType(ServerStateEnum currentState) {
        return stateMap.get(currentState);
    }
}
