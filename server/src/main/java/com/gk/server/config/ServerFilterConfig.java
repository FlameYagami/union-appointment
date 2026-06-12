package com.gk.server.config;

import com.gk.server.filter.ServerApiFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;

/**
 * 接口拦截器配置
 *
 * @author GuoYu
 * @since 2023-03-09 11:27
 */

@Configuration
public class ServerFilterConfig {

    @Resource
    private ServerApiFilter serverApiFilter;

    @Bean
    public FilterRegistrationBean<Filter> serverApiFilterRegister() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(serverApiFilter);
        // 设置url过滤器 匹配的url
        registration.addUrlPatterns("/api/*");
        registration.setName("serverApiFilter");
        return registration;
    }

}
