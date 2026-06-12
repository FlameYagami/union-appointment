package com.gk.openapi.config;

import com.gk.openapi.filter.OpenApiFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 平台过滤器配置
 *
 * @author Flame
 * @date 2023-03-14 16:44
 **/
@Configuration
public class OpenApiFilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> tpFilterRegister() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(platformApiFilter());
        registration.addUrlPatterns("/open-api/*"); // 设置url过滤器 匹配的url
        registration.setName("opeApiFilter"); // 名称
        return registration;
    }

    /**
     * 定义filter对应的bean
     */
    @Bean
    public Filter platformApiFilter() {
        return new OpenApiFilter();
    }

}
