package com.gk.common.intf;

/**
 * 配置接口类(每一个业务模块都需要创建一个XxxConfig, 并实现此接口)
 *
 * @author Flame
 * @since 2024-08-26 15:27
 **/
public interface IConfig {

    /**
     * 数据库 config_key 字段
     */
    String getName();

    /**
     * 数据库 config_key 字段
     */
    String getKey();

    /**
     * 数据库 config_value 字段
     */
    String getValue();

}
