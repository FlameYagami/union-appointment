package com.gk.app.interceptor;

import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.model.others.BaseResult;
import com.gk.common.model.others.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

/**
 * 全局异常捕获类
 * <p>
 * {@link com.gk.framework.interceptor.GlobalSysExceptionHandler}
 * {@link com.gk.security.interceptor.GlobalSecurityExceptionHandler}
 *
 * @author Flame
 * @date 2023-02-02 11:00
 **/

@RestControllerAdvice
@Slf4j
@Order(99)
public class GlobalAppExceptionHandler {

    private static final List<Integer> infos = Arrays.asList(
            SysStatus.ACCOUNT_OR_PASSWORD_ERROR.getCode(),
            SysStatus.PASSWORD_ILLEGAL.getCode()
    );

    @ExceptionHandler(SysException.class)
    public BaseResult handleSysException(SysException exception) {
        // 单独管理info级别的打印
        if (infos.contains(exception.getCode())) {
            log.info("System Exception: {}", exception.getMessage());
        } else {
            log.error("System Exception: {}", exception.getMessage());
        }
        if (null == exception.getData()) {
            return BaseResult.error(exception.getCode(), exception.getMessage());
        }
        return ApiResult.error(exception.getCode(), exception.getMessage(), exception.getData());
    }

    @ExceptionHandler(value = Exception.class)
    public BaseResult handlerDefaultException(Exception exception) {
        log.error("Default Exception: ", exception);
        return BaseResult.error(SysStatus.FAILED);
    }

}
