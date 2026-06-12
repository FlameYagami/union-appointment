package com.gk.security.interceptor;

import com.gk.common.enums.SysStatus;
import com.gk.common.model.others.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常捕获类
 * <p>
 * {@link com.gk.framework.interceptor.GlobalSysExceptionHandler}
 *
 * @author Flame
 * @date 2023-02-02 11:00
 **/

@RestControllerAdvice
@Slf4j
@Order(2)
public class GlobalSecurityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public BaseResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("{}: {}", SysStatus.PERMISSION_ERROR.getMessage(), requestURI);
        return BaseResult.error(SysStatus.PERMISSION_ERROR);
    }

}
