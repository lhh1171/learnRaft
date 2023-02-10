package com.lhhraft.core.config.loader.config;

import com.google.common.collect.Lists;
import com.lhhraft.core.config.loader.base.BaseToString;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description: 配置model
 */
@Getter
@Setter
public class ConfigModel extends BaseToString {
    private static final long serialVersionUID = 7825937885052180314L;

    private RaftNodeModel localNode;
    private List<RaftNodeModel> allNodes;

    /**
     * 获取当前server的id
     *
     * @return id
     */
    public Long getCurrentServerId() {
        return localNode.getServerId();
    }

    /**
     * 获取所有远程服务器配置
     */
    public List<RaftNodeModel> getRemoteNodes() {
        List<RaftNodeModel> remoteNodes = Lists.newArrayList();
        remoteNodes.addAll(allNodes);
        remoteNodes.removeIf(node -> node.getServerId().equals(localNode.getServerId()));
        return remoteNodes;
    }


}
