package com.lhhraft.raft.model;

import com.lhhraft.raft.base.BaseToString;
;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 日志条目
 */
@Getter
@Setter
public class LogEntryModel extends BaseToString {
    private static final long serialVersionUID = 7076763685587042394L;

    private Long index;
    private Long term;

    /**
     * 操作类型
     */
    private String option;

    /**
     * 操作数据
     */
    private String data;
}
