package com.lhhraft.core.config.loader.model;

import com.lhhraft.core.config.loader.base.BaseToString;;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class SubmitResponse extends BaseToString {
    private static final long serialVersionUID = -1500206787222543731L;

    /**
     * 提交是否成功
     */
    private Boolean success;

    /**
     * 提交失败时，使用该节点提交
     */
    private Long leaderId;
}
