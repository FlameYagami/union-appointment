package com.gk.framework.filter;

import cn.hutool.extra.servlet.ServletUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求适配器 基础类
 *
 * @author Flame
 * @since 2025-07-23 16:58
 */
public class BaseServletRequestWrapper extends AbstractServletRequestWrapper {

    /**
     * 请求参数
     */
    private final byte[] body;

    public BaseServletRequestWrapper(HttpServletRequest request) {
        super(request);
        body = ServletUtil.getBodyBytes(request);
    }

    @Override
    protected byte[] getBody() {
        return body;
    }

}
