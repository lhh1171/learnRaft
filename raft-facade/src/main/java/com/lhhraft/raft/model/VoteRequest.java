package com.lhhraft.raft.model;

import com.lhhraft.raft.base.BaseToString;;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 投票请求对象
 */
@Getter
@Setter
public class VoteRequest extends BaseToString {
    private static final long serialVersionUID = -355819569838316428L;

    /**
     * candidate 的任期号
     */
    private Long term;

    /**
     * 请求选票的candidate id
     */
    private Long candidateId;

    /**
     * candidate的最后一条 log entry 的index
     */
    private Long lastLogIndex;

    /**
     * candidate的最后一条 log entry 的term
     */
    private Long lastLogTerm;

}
