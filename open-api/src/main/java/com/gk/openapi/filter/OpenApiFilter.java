package com.gk.openapi.filter;

import cn.hutool.core.util.StrUtil;
import com.gk.common.utils.ServletExtUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 平台过滤器
 *
 * 扫描包路径，凡是在/open-api/*下的接口都会被拦截
 * @author Flame
 * @date 2023-03-14 16:44
 **/
public class OpenApiFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String method = request.getMethod();

        // 该方法处理 GET
        if (HttpMethod.GET.matches(method)) {
            try {
                OpenApiRespWrapper openApiRespWrapper = new OpenApiRespWrapper(response);
                filterChain.doFilter(servletRequest, openApiRespWrapper);
                openApiRespWrapper.encryptData(request, response);
            } catch (Exception e) {
                ServletExtUtils.responseJson(response, e);
            }
            return;
        }

        // 该方法处理 POST、PUT、PATCH、DELETE请求并且contentType为application/json格式的
        String contentType = request.getContentType();
        if (StrUtil.isNotEmpty(contentType)) {
            contentType = contentType.toLowerCase();
        }
        if ((HttpMethod.POST.matches(method)
                || HttpMethod.PUT.matches(method)
                || HttpMethod.PATCH.matches(method)
                || HttpMethod.DELETE.matches(method))
                && StrUtil.isNotEmpty(contentType) && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            try {
                servletRequest = new OpenApiReqWrapper(request);
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (Exception e) {
                ServletExtUtils.responseJson(response, e);
            }
            return;
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            ServletExtUtils.responseJson((HttpServletResponse) servletResponse, e);
        }
    }

    @Override
    public void destroy() {
    }
}

