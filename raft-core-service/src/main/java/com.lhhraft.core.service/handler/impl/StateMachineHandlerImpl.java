package com.lhhraft.core.service.handler.impl;

import com.lhhraft.raft.facade.model.LogEntryModel;
import com.lhhraft.raft.facade.model.RaftCoreModel;
import com.lhhraft.raft.facade.model.ServerStateModel;
import com.lhhraft.core.service.handler.StateMachineHandler;
import com.lhhraft.state.machine.StateMachine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * @description: 状态机控制器

 */
@Slf4j
@Service
public class StateMachineHandlerImpl implements StateMachineHandler {
    @Resource
    private StateMachine stateMachine;

    @Override
    public void commit2Apply() {

        while (!Thread.currentThread().isInterrupted()) {
            Lock lock = RaftCoreModel.getLock();
            lock.lock();
            try {
                //0.数据准备,拿到coreModel
                RaftCoreModel coreModel = RaftCoreModel.getSingleton();
                List<LogEntryModel> entries = coreModel.getPersistentState().getLogEntries();
                ServerStateModel serverState = coreModel.getServerState();

                //1.阻塞等待 take:若队列为空，发生阻塞，等待有元素。
                coreModel.getCommitChannel().take();

                //2.接到通知后，apply 到状态机：将logs[lastApplied+1, commitIndex] apply
                for (long i = serverState.getLastApplied() + 1;
                     i <= serverState.getCommitIndex(); i++) {
                    stateMachine.execute(entries.get((int) i));
                    serverState.setLastApplied(serverState.getLastApplied() + 1);
                }
            } catch (Exception e) {
                log.error("commit2Apply error", e);
            } finally {
                lock.unlock();
            }
        }
    }

}
