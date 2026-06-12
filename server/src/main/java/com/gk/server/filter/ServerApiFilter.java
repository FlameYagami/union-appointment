package com.gk.server.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.gk.common.enums.EnabledType;
import com.gk.common.utils.ServletExtUtils;
import com.gk.framework.enums.SystemConfig;
import com.gk.framework.filter.BaseServletRequestWrapper;
import com.gk.framework.helper.SysConfigHelper;
import com.gk.security.model.bo.SecurityParam;
import com.gk.server.model.others.CryptoParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 业务模块 接口加解密过滤器
 *
 * @author GuoYu
 * @since 2023-03-09 11:41
 */
@Component
@Slf4j
@Order(2)
public class ServerApiFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 判断是否为提交请求(不包含文件上传)
        boolean isSubmitMethod = isSubmitMethod(request);

        // 判断是否白名单|是否加解密
        if (isWhiteListUrl(request) || !EnabledType.ENABLE.value.equals(SysConfigHelper.getInstance().getString(SystemConfig.CRYPTO_ENABLE))) {
            // 非GET方法走基类请求适配器(防重复提交针对非Get请求)
            if (isSubmitMethod) {
                servletRequest = new BaseServletRequestWrapper(request);
            }
            try {
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (Exception exception) {
                ServletExtUtils.handleException(response, exception);
            }
            return;
        }

        // GET方法只加密结果(防重复提交针对非Get请求)
        if (HttpMethod.GET.matches(request.getMethod())) {
            try {
                ServerRespWrapper serverRespWrapper = new ServerRespWrapper(response);
                filterChain.doFilter(servletRequest, serverRespWrapper);
                serverRespWrapper.encryptData(response);
            } catch (Exception exception) {
                ServletExtUtils.handleException(response, exception);
            }
            return;
        }

        // POST、PUT、PATCH、DELETE请求并且contentType为application/json格式的请求只处理请求参数
        if (isSubmitMethod) {
            try {
                servletRequest = new ServerReqWrapper(request);
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (Exception exception) {
                ServletExtUtils.handleException(response, exception);
            }
            return;
        }

        // 其他方法例如:文件上传.防重复提交可能无法适用
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception exception) {
            ServletExtUtils.handleException(response, exception);
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * 是否为白名单地址
     */
    private boolean isWhiteListUrl(HttpServletRequest request) {
        // URI白名单过滤 不进行加解密
        String requestUri = request.getRequestURI();
        SecurityParam securityParam = SpringUtil.getBean(SecurityParam.class);
        CryptoParam cryptoParam = SpringUtil.getBean(CryptoParam.class);
        // 加解密白名单
        Set<String> whiteListSet = new HashSet<>();
        // 匿名访问接口列表不进行加解密
        whiteListSet.addAll(List.of(securityParam.getAnonymous()));
        // 加解密白名单列表不进行加解密
        whiteListSet.addAll(List.of(cryptoParam.getWhiteList()));
        // 使用AntPathMatcher做正则匹配
        AntPathMatcher antMatcher = new AntPathMatcher();
        return whiteListSet.stream().anyMatch(it -> antMatcher.match(it, requestUri));
    }

    /**
     * 是否为提交类型的请求
     */
    private boolean isSubmitMethod(HttpServletRequest request) {
        // 请求方式
        String method = request.getMethod();
        // 内容类型
        String contentType = request.getContentType();
        return (HttpMethod.POST.matches(method)
                || HttpMethod.PUT.matches(method)
                || HttpMethod.PATCH.matches(method)
                || HttpMethod.DELETE.matches(method))
                && StrUtil.isNotEmpty(contentType)
                && contentType.toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE);
    }

}

