package com.lhhraft.core.config.loader.model;

import com.lhhraft.core.config.loader.base.BaseToString;import com.lhhraft.core.config.loader.base.BaseToString;;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class AppendEntriesResponse extends BaseToString {
    private static final long serialVersionUID = 3110625658565930253L;

    public AppendEntriesResponse(Long term, Boolean success) {
        this.term = term;
        this.success = success;
    }

    /**
     * 当前term，for leader update itself
     */
    private Long term;

    /**
     * 跟随者包含了 匹配上 prevLogIndex 和 prevLogTerm 的日志时为真
     */
    private Boolean success;

}
