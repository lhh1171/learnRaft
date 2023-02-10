package com.lhhraft.state.machine.impl;


import com.google.common.collect.Maps;
import com.lhhraft.core.config.loader.constant.CommonConstant;
import com.lhhraft.core.config.loader.enums.OptionEnum;
import com.lhhraft.core.config.loader.exception.ErrorCodeEnum;
import com.lhhraft.core.config.loader.exception.RaftException;
import com.lhhraft.core.config.loader.model.LogEntryModel;
import com.lhhraft.core.config.loader.util.CommonUtil;
import com.lhhraft.state.machine.StateMachine;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description: key-value 的状态机实现
 */
@Log4j
@Service
public class KvStateMachineImpl implements StateMachine {

    private final Map<String, String> kvMap = Maps.newHashMap();

    @Override
    public void execute(LogEntryModel logEntryModel) {
        OptionEnum optionEnum = OptionEnum.getByCode(logEntryModel.getOption());
        // PUT 操作时，有key和value两个值
        List<String> kvData = CommonUtil.split(logEntryModel.getData(), CommonConstant.DATA_SPLIT_FLAG);
        switch (optionEnum) {
            case GET:
                log.info("GET option :" + kvData);
                break;
            case ADD:
                log.info("ADD option :" + kvData);
                kvMap.put(kvData.get(0), kvData.get(1));
                break;
            case DEL:
                log.info("DEL option :" + kvData);
                kvMap.remove(kvData.get(0));
                break;
            default:
                throw new RaftException(ErrorCodeEnum.NOT_SUPPORT_TYPE, "不支持的类型:" + optionEnum);
        }

    }
}
