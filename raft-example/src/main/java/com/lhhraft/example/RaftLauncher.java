package com.lhhraft.example;

import com.lhhraft.raft.exception.RaftException;
import com.lhhraft.core.service.handler.StateMachineHandler;
import com.lhhraft.core.service.transformer.ServerStateTransformerStarter;
import com.lhhraft.example.provider.DubboServiceRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Slf4j
@Component
public class RaftLauncher {
    @Resource
    private ServerStateTransformerStarter serverStateTransformerStarter;

    @Resource
    private StateMachineHandler stateMachineHandler;

    @Resource
    private DubboServiceRegister dubboServiceRegister;

    /**
     * 这里的启动有先后顺序
     */
    @PostConstruct
    public void start() {
        try {
            //1. 启动dubbo服务
            //暴露自己的dubbo服务，把RaftFacade和OperationFacade对象都注册进去了
            //RaftFacade requestVote 请求投票  appendEntries添加条目（附加日志）
            //OperationFacade submitData 提交数据
            dubboServiceRegister.registry();

            //2. 启动server 状态流转  节点状态转换器启动（即角色转换状态机）
            //ServerStateTransformer AbstractServerStateTransformer (CandidateStateImpl,FollowerStateImpl,LeaderStateImpl)
            //有一个executeNext遍历执行的工作
            serverStateTransformerStarter.start();

            //3. 启动状态机
            stateMachineHandler.commit2Apply();
        } catch (RaftException raftException) {
            log.error(raftException.getErrorMsg(), raftException);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        }
    }
    public static void main(String[] args) {
        //通过读取xml或者properties文件获取集群节点信息、

        //直接加入到raft组里，主动上报自己的节点信息
        RaftLauncher raftLauncher=new RaftLauncher();
        raftLauncher.start();
    }
}
