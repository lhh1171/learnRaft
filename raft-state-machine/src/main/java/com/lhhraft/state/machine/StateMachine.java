package com.lhhraft.state.machine;


import com.lhhraft.core.config.loader.model.LogEntryModel;

/**
 * @description: 状态机

 */
public interface StateMachine {
    /**
     * 状态机执行
     *
     * @param logEntryModel log条目
     */
    void execute(LogEntryModel logEntryModel);
}
