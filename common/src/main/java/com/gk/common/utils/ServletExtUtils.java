package com.gk.common.utils;

import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * 客户端工具类
 *
 * @author GuoYu
 * @since 2023-01-13 10:31
 */

@Slf4j
public class ServletExtUtils extends ServletUtil {

    private ServletExtUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = ServletExtUtils.getRequestAttributes();
        if (requestAttributes == null) {
            throw new SysException(SysStatus.FAILED);
        }
        return requestAttributes.getRequest();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 将对象转换成json响应接口
     *
     * @param response 接口响应
     * @param jsonString 响应返回的JSON数据字符串
     */
    public static void responseJson(HttpServletResponse response, int httpCode, String jsonString) {
        try {
            response.setStatus(httpCode);
            response.setContentType(ContentType.JSON.toString());
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.getWriter().print(jsonString);
        } catch (Exception e) {
            log.error("Response Json Error: {}", e.getMessage());
        }
    }

    /**
     * 将对象转换成json响应接口
     *
     * @param response 接口响应
     * @param jsonString 响应返回的JSON数据字符串
     */
    public static void responseJson(HttpServletResponse response, String jsonString) {
        responseJson(response, 200, jsonString);
    }

    /**
     * 将对象转换成json响应接口
     *
     * @param response 接口响应
     * @param httpCode 返回码
     * @param object   响应返回的数据对象
     */
    public static void responseJson(HttpServletResponse response, int httpCode, Object object) {
        responseJson(response, httpCode, JacksonUtils.toJson(object));
    }

    /**
     * 将对象转换成json响应接口
     *
     * @param response 接口响应
     * @param object 响应返回的数据对象
     */
    public static void responseJson(HttpServletResponse response, Object object) {
        responseJson(response, JacksonUtils.toJson(object));
    }


    /**
     * 处理抛出异常的响应
     *
     * @param response 接口响应
     * @param exception 异常对象
     */
    public static void handleException(HttpServletResponse response, Exception exception) {
        if (exception instanceof SysException) {
            SysException sysException = (SysException) exception;
            log.error("System Exception: {}", sysException.getMessage());
            if (null == sysException.getData()) {
                ServletExtUtils.responseJson(response, BaseResult.error(sysException.getCode(), sysException.getMessage()));
            } else {
                ServletExtUtils.responseJson(response, ApiResult.error(sysException.getCode(), sysException.getMessage(), sysException.getData()));
            }
        } else {
            log.error("Default Exception: ", exception);
            ServletExtUtils.responseJson(response, BaseResult.error(SysStatus.FAILED));
        }
    }
}
