package com.lhhraft.raft.facade.constant;

/**
 * @description: 常量
 */
public class CommonConstant {

    /**
     * 通知channel 标志
     */
    public static final String CHANNEL_FLAG = "FLAG";

    /**
     * 初始term值
     */
    public static final Long INIT_TERM = 0L;

    /**
     * 初始index
     */
    public static final Long INIT_INDEX = 0L;

    /**
     * 心跳间隔时间，单位ms
     */
    public static final int HEARTBEAT_INTERVAL = 1000;

    /**
     * 数据分割标识
     */
    public static final String DATA_SPLIT_FLAG = " ";

}