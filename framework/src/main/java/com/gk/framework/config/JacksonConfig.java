package com.gk.framework.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gk.common.constant.DateConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Jackson配置类
 *
 * @author GuoYu
 * @since 2022-10-09 10:09
 **/

@Configuration
@Slf4j
public class JacksonConfig {
    /**
     * Jackson全局转化Long类型为String，解决jackson序列化时传入前端Long类型缺失精度问题
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // Long类型 自动转换成 String类型
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
            builder.serializerByType(BigInteger.class, ToStringSerializer.instance);

            builder.dateFormat(new SimpleDateFormat(DateConstant.DEFAULT_PATTERN));
            builder.timeZone(TimeZone.getTimeZone(DateConstant.DEFAULT_TIME_ZONE));
            builder.failOnUnknownProperties(false);
            builder.failOnEmptyBeans(false);
        };
    }

}
