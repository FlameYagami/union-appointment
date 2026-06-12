package com.gk.framework.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 请求适配器 抽象类
 *
 * @author Flame
 * @since 2025-07-23 15:24
 */
public abstract class AbstractServletRequestWrapper extends HttpServletRequestWrapper {

    protected abstract byte[] getBody();

    public AbstractServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream stream = new ByteArrayInputStream(getBody());
        return new ServletInputStream() {
            @Override
            public int read() {
                return stream.read();
            }

            @Override
            public boolean isFinished() {
                return stream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    /**
     * 获取 body 字符串
     */
    public String getBodyString() {
        return new String(getBody(), StandardCharsets.UTF_8);
    }

}
