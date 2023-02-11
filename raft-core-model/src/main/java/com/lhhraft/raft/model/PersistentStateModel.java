package com.lhhraft.raft.model;

import com.lhhraft.raft.base.BaseToString;
import com.lhhraft.raft.constant.CommonConstant;
import com.lhhraft.raft.exception.ErrorCodeEnum;
import com.lhhraft.raft.exception.RaftException;
;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

/**
 * @description: 持久化的状态数据
 */
@Getter
@Setter
public class PersistentStateModel extends BaseToString {
    private static final long serialVersionUID = 614565993931190984L;

    private Long currentTerm;

    /**
     * 在 currentTerm 获得选票的serverId。如果没有投票则为null
     * 改变 votedFor 的两种情况：
     * *   一是 Follower/Candidate 超时变为 Candidate 时，term 会增加 1，这时候先无脑投自己（rf.votedFor = rf.me），然后发起选举；
     * *   二是在收到其他 Peer 的 RPC 时（包括 Request 和 Reply），发现别人 term 高，变为 Follower 时，也需要及时清空自己之前投票结果（rf.votedFor = null）以使本轮次可以继续投票。
     */
    private Long votedFor;

    private List<LogEntryModel> logEntries;

    /*==============================辅助函数=============================*/

    /**
     * 获取最后一条写入的entry
     */
    public LogEntryModel getLastEntry() {
        return logEntries.get(logEntries.size() - 1);
    }


    /**
     * 获取倒数第二条写入的entry
     */
    public LogEntryModel getPreEntry() {
        //只有1条记录(初始记录)时，直接返回该记录
        if (logEntries.size() == 1
                && logEntries.get(0).getIndex().equals(CommonConstant.INIT_INDEX)) {
            return logEntries.get(0);
        }
        return logEntries.get(logEntries.size() - 2);
    }

    /**
     * 根据index获取term，不存在则抛异常
     */
    public Long getTermByIndex(Long index) {
        if (getLastEntry().getIndex() > index) {
            throw new RaftException(ErrorCodeEnum.DATA_NOT_EXIT, "index过大，当前server不存在");
        }
        return logEntries.get(Math.toIntExact(index)).getTerm();
    }
}
