package com.gk.sms.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * Created by Flame on 2020/03/18.
 **/
@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SMSInfo {

    private String userId;
    private String apiKey;
    private String ts;
    private String sign;

    // 以下为自定义属性,需要填充
    private String mobile;
    private String content;
}
