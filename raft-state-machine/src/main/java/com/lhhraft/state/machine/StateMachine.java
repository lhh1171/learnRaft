package com.lhhraft.state.machine;


import com.lhhraft.raft.facade.model.LogEntryModel;

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
