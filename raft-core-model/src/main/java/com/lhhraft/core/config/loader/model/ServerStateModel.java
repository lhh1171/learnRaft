package com.lhhraft.core.config.loader.model;

import lombok.Getter;
import lombok.Setter;
import com.lhhraft.core.config.loader.base.BaseToString;
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
