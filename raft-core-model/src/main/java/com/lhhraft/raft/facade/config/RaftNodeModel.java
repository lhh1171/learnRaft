package com.lhhraft.raft.facade.config;

import com.lhhraft.raft.facade.base.BaseToString;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: raft 节点配置
 */
@Getter
@Setter
public class RaftNodeModel extends BaseToString {
    private static final long serialVersionUID = -2921302830680312336L;

    /**
     * rpc server id
     */
    private Long serverId;

    /**
     * rpc通讯的ip
     */
    private String ip;

    /**
     * rpc通讯的端口
     */
    private Integer port;

}
