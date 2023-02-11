package com.lhhraft.raft.facade.model;

import com.lhhraft.raft.facade.base.BaseToString;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: server状态数据

 */
@Getter
@Setter
public class ServerStateModel extends BaseToString {
    private static final long serialVersionUID = -2537321438890981740L;

    private Long commitIndex;
    private Long lastApplied;
}
