package com.gk.server.enums;

import com.gk.common.constant.CommonConstant;
import com.gk.common.intf.IConfig;
import lombok.Getter;

/**
 * 服务模块配置类
 *
 * @author Flame
 * @since 2024-08-26 15:27
 **/
@Getter
public enum ServerConfig implements IConfig {

    /**
     * 不适用(占位配置项)
     */
    NA("NA", "NA", CommonConstant.EMPTY);

    /**
     * 数据库 config_name 字段
     */
    public final String name;

    /**
     * 数据库 config_key 字段
     */
    public final String key;

    /**
     * 数据库 config_value 字段
     */
    public final String value;

    ServerConfig(String name, String key, String value) {
        this.name = name;
        this.key = key;
        this.value = value;
    }


}
