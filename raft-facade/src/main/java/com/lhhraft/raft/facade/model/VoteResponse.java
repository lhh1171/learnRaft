package com.lhhraft.raft.facade.model;

import com.lhhraft.raft.facade.base.BaseToString;;
import lombok.Getter;
import lombok.Setter;

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
