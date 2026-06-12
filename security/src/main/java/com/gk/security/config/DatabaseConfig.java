package com.gk.security.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.gk.security.handler.DatabaseFieldHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库默认字段自动填充配置类
 *
 * @author GuoYu
 * @since 2022-12-28 14:13
 **/
@Configuration
public class DatabaseConfig {

    /**
     * 自动填充字段处理类
     */
    @Bean
    public MetaObjectHandler defaultMetaObjectHandler(){
        return new DatabaseFieldHandler();
    }
}
