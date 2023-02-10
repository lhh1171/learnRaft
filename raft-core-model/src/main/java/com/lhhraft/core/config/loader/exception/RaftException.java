package com.lhhraft.core.config.loader.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @description: 决策异常
 */
@Getter
@Setter
public class RaftException extends RuntimeException {

    private static final long serialVersionUID = 6100413904443364600L;

    /**
     * 错误码枚举
     */
    private ErrorCodeEnum errorCodeEnum;

    /**
     * 详细错误信息
     */
    private String detailErrorMsg;


    /**
     * 原始的异常信息
     */
    private Throwable originalThrowable;


    public RaftException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getCode());
        this.errorCodeEnum = errorCodeEnum;
    }

    public RaftException(ErrorCodeEnum errorCodeEnum, String detailErrorMsg) {
        super(errorCodeEnum.getCode());
        this.errorCodeEnum = errorCodeEnum;
        this.detailErrorMsg = detailErrorMsg;
    }

    public RaftException(ErrorCodeEnum errorCodeEnum, Throwable originalThrowable) {
        super(errorCodeEnum.getCode(), originalThrowable);
        this.errorCodeEnum = errorCodeEnum;
        this.originalThrowable = originalThrowable;
    }

    public RaftException(ErrorCodeEnum errorCodeEnum, String detailErrorMsg, Throwable originalThrowable) {
        super(errorCodeEnum.getCode(), originalThrowable);
        this.errorCodeEnum = errorCodeEnum;
        this.detailErrorMsg = detailErrorMsg;
        this.originalThrowable = originalThrowable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName()).append(", ");
        sb.append(this.errorCodeEnum.getCode()).append(", ");
        sb.append(this.errorCodeEnum.getDesc()).append(", ");
        sb.append(this.detailErrorMsg);
        return String.valueOf(sb);
    }

    public String getErrorMsg() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.errorCodeEnum.getDesc()).append(", ");
        sb.append(this.detailErrorMsg);
        return String.valueOf(sb);
    }

}
