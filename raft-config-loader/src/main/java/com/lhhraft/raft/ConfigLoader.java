package com.lhhraft.raft;

import cn.hutool.core.io.FileUtil;
import com.lhhraft.raft.config.ConfigModel;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;


import java.io.BufferedReader;


@Service
public class ConfigLoader {
    private static final ConfigModel CONFIG_MODEL;

    static {
        BufferedReader bufferedReader = FileUtil.getReader(
                "server-rpc-config.yml",
                "UTF-8");
        Yaml yaml = new Yaml();
        CONFIG_MODEL = yaml.loadAs(bufferedReader, ConfigModel.class);
    }

    /**
     * 加载配置信息
     *
     * @return 配置
     */
    public static ConfigModel load() {
        return CONFIG_MODEL;
    }

    /**
     * 获取server的总数量
     * 逻辑：自身1个+远程个数
     *
     * @return 数量
     */
    public static Integer getServerCount() {
        return CONFIG_MODEL.getAllNodes().size();
    }
}
