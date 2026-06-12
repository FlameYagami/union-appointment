package com.gk.security.model.bo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * spring security 参数配置类
 *
 * @author GuoYu
 * @since 2022-12-27 09:52
 **/

@Component
@ConfigurationProperties(prefix = "security-config")
@Data
public class SecurityParam {
    /**
     * 匿名访问配置
     */
    private String[] anonymous;

    /**
     * 任意访问配置
     */
    private String[] permitAll;
}
