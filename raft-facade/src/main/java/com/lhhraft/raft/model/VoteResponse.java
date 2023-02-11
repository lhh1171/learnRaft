package com.lhhraft.raft.model;

import com.lhhraft.raft.base.BaseToString;
;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 投票返回对象
 */
@Getter
@Setter
public class VoteResponse extends BaseToString {
    private static final long serialVersionUID = 2860045664738196559L;

    public VoteResponse(Long term, Boolean voteGranted) {
        this.term = term;
        this.voteGranted = voteGranted;
    }

    private Long term;
    private Boolean voteGranted;


}
