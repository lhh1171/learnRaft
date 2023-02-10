package com.lhhraft.core.service.transformer.impl;

import com.google.common.collect.Lists;
import com.lhhraft.core.config.loader.enums.ServerStateEnum;
import com.lhhraft.core.config.loader.exception.ErrorCodeEnum;
import com.lhhraft.core.config.loader.exception.RaftException;
import com.lhhraft.core.config.loader.model.RaftCoreModel;
import com.lhhraft.core.config.loader.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description: follower状态
 */
@Slf4j
@Service(value = "followerStateImpl")
public class FollowerStateImpl extends AbstractServerStateTransformer {

    @Override
    public void execute() {
        while (!Thread.currentThread().isInterrupted()) {
            log.info("当前为Follower：" + RaftCoreModel.getSingleton());
            String channelFlag;
            try {
                //1.每过6~10个心跳时间获取一次心跳标志
                channelFlag = RaftCoreModel.getSingleton()
                        .getHeartbeatChannel()
                        .poll(CommonUtil.getInterval(6, 10), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RaftException(ErrorCodeEnum.CHANNEL_ERROR, "定时获取channelFlag异常");
            }

            //2. 判断通知结果
            //如果心跳超时（结果为空），进行下一个状态；否则继续循环
            if (StringUtils.isBlank(channelFlag)) {
                RaftCoreModel.getSingleton().setServerStateEnum(ServerStateEnum.CANDIDATE);
            }

            //3.执行后续节点
            executeNext();
        }
    }

    @Override
    public void preDo() {
        // do nothing
    }

    @Override
    public List<ServerStateEnum> getNextStates() {
        return Lists.newArrayList(
                ServerStateEnum.CANDIDATE
        );
    }

    @Override
    public ServerStateEnum getCurrentState() {
        return ServerStateEnum.FOLLOWER;
    }

}
