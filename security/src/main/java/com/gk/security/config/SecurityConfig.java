package com.gk.security.config;

import com.gk.security.filter.SysUserTokenFilter;
import com.gk.security.handler.AuthenticationHandler;
import com.gk.security.handler.LogoutHandler;
import com.gk.security.model.bo.SecurityParam;
import com.gk.security.service.SysUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;

/**
 * spring security配置
 *
 * @author Flame
 * @since 2022-11-20 10:16
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    /**
     * spring security参数配置
     */
    @Resource
    private SecurityParam securityParam;

    /**
     * 认证失败处理类
     */
    @Resource
    private AuthenticationHandler authenticationHandler;

    /**
     * 退出处理类
     */
    @Resource
    private LogoutHandler logoutHandler;

    /**
     * 用户认证逻辑
     */
    @Resource
    private SysUserDetailsService sysUserDetailsService;

    /**
     * 用户token认证过滤器
     */
    @Resource
    private SysUserTokenFilter sysUserTokenFilter;

    /**
     * 跨域过滤器
     */
    @Resource
    private CorsFilter corsFilter;

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CSRF禁用，因为不使用session
                .csrf().ignoringAntMatchers("/druid/*") // Druid监控接口
                .disable()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(authenticationHandler)
                // 基于token，所以不需要session
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests() // 过滤请求
                // 允许匿名访问
                .antMatchers(securityParam.getAnonymous()).anonymous()
                // 禁止访问
                .antMatchers(HttpMethod.GET, securityParam.getPermitAll()).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();
        httpSecurity.logout().logoutUrl("/api/logout").logoutSuccessHandler(logoutHandler);
        // 添加JWT filter
        httpSecurity.addFilterBefore(sysUserTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        httpSecurity.addFilterBefore(corsFilter, SysUserTokenFilter.class);
        httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(sysUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder())
                .and()
                .build();
    }

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
