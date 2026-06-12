package com.gk.server.model.others;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 接口加解密配置类
 *
 * @author GuoYu
 * @since 2023-03-10 16:24
 **/

@Component
@ConfigurationProperties(prefix = "server-crypto")
@Data
public class CryptoParam {
    /**
     * 加密接口白名单
     */
    private String[] whiteList;
}
