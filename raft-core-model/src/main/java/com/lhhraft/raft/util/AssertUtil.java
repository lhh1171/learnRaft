package com.lhhraft.raft.util;

import com.alibaba.fastjson.util.TypeUtils;
import com.lhhraft.raft.exception.ErrorCodeEnum;
import com.lhhraft.raft.exception.RaftException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @description: 工具类

 */
public class AssertUtil {
    /**
     * str 不能为空
     *
     * @param str       校验参数
     * @param errorDesc 异常描述
     */
    public static void notBlank(String str, String errorDesc) {
        if (StringUtils.isBlank(str)) {
            throw new RaftException(ErrorCodeEnum.PARAM_ERROR, errorDesc);
        }
    }

    /**
     * str 必须能为空
     *
     * @param str       校验参数
     * @param errorDesc 异常描述
     */
    public static void mustBeBlank(String str, String errorDesc) {
        if (StringUtils.isNotBlank(str)) {
            throw new RaftException(ErrorCodeEnum.PARAM_ERROR, errorDesc);
        }
    }

    /**
     * list 不能为空
     *
     * @param list      校验参数
     * @param errorDesc 异常描述
     */
    public static void notEmpty(List list, String errorDesc) {
        if (CollectionUtils.isEmpty(list)) {
            throw new RaftException(ErrorCodeEnum.PARAM_ERROR, errorDesc);
        }
    }

    /**
     * list 必须能为空
     *
     * @param list      校验参数
     * @param errorDesc 异常描述
     */
    public static void mustBeEmpty(List list, String errorDesc) {
        if (!CollectionUtils.isEmpty(list)) {
            throw new RaftException(ErrorCodeEnum.PARAM_ERROR, errorDesc);
        }
    }

    /**
     * 不能为空
     *
     * @param ob        校验参数
     * @param errorDesc 异常描述
     */
    public static void notNull(Object ob, String errorDesc) {
        if (Objects.isNull(ob)) {
            throw new RaftException(ErrorCodeEnum.PARAM_ERROR, errorDesc);
        }
    }

    /**
     * 不能为false
     *
     * @param ob        校验参数
     * @param errorDesc 异常描述
     */
    public static void assertTrue(Object ob, String errorDesc) {
        if (!TypeUtils.castToBoolean(ob)) {
            throw new RaftException(ErrorCodeEnum.PARAM_ERROR, errorDesc);
        }
    }
}
