package com.lhhraft.core.config.loader.util;


import com.lhhraft.core.config.loader.constant.CommonConstant;
import com.lhhraft.core.config.loader.model.LogEntryModel;

/**
 * @description: 条目相关工具类

 */
public class EntryUtil {
    /**
     * 获取初始term，即第0个entry
     *
     * @return entry
     */
    public static LogEntryModel getInitEntry() {
        LogEntryModel logEntryModel = new LogEntryModel();
        logEntryModel.setIndex(CommonConstant.INIT_INDEX);
        logEntryModel.setTerm(CommonConstant.INIT_TERM);
        return logEntryModel;
    }
}
