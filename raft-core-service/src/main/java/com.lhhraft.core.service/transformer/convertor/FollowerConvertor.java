package com.lhhraft.core.service.transformer.convertor;


import com.lhhraft.core.config.loader.enums.ServerStateEnum;
import com.lhhraft.core.config.loader.model.RaftCoreModel;

/**
 * @description: follower 转换
 */
public class FollowerConvertor {
    /**
     * 转换为follower： 设置相关变量
     * 说明：加锁在调用方完成
     *
     * @param term      需要转换为的term
     * @param coreModel 核心对象
     */
    public static void convert2Follower(Long term,
                                        RaftCoreModel coreModel) {
        coreModel.setServerStateEnum(ServerStateEnum.FOLLOWER);
        coreModel.getPersistentState().setVotedFor(null);
        coreModel.getPersistentState().setCurrentTerm(term);
    }
}
