package com.gk.framework.interceptor;

import cn.hutool.core.util.StrUtil;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.others.BaseResult;
import com.gk.common.utils.StringExtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 系统异常捕获类
 *
 * @author Kevin
 * @date 2020-03-24 10:50
 **/

@RestControllerAdvice
@Slf4j
@Order(1)
public class GlobalSysExceptionHandler {

    private final boolean devActive;

    public GlobalSysExceptionHandler(Environment environment) {
        // 开发环境判定
        devActive = "dev".equals(environment.getProperty("spring.profiles.active"));
    }

    @ExceptionHandler(BindException.class)
    public BaseResult handleBindException(BindException exception) {
        StringBuilder desc = new StringBuilder();
        String defaultMsg;
        for (FieldError field : exception.getFieldErrors()) {
            defaultMsg = devActive
                    ? StrUtil.format("{}{}: {}, ", field.getField(), field.getDefaultMessage(), field.getRejectedValue())
                    : StrUtil.format("{}: {}, ", field.getDefaultMessage(), field.getRejectedValue());
            desc.append(defaultMsg);
        }
        // 删除最后的", "部分
        desc.delete(desc.length() - 2, desc.length());
        log.error("Bind Exception: {}", desc);
        String message = SysStatus.INVALID_PARAM.getMessage() + desc;
        return BaseResult.error(SysStatus.INVALID_PARAM.getCode(), message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResult handleValidationException(ConstraintViolationException exception) {
        int resultCode = SysStatus.INVALID_PARAM.getCode();
        String resultDesc = StringExtUtils.joinComma(
                exception.getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList()));
        log.error("Validation Exception: {}", resultDesc);
        return BaseResult.error(resultCode, SysStatus.INVALID_PARAM.getMessage() + resultDesc);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResult handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        StringBuilder desc = new StringBuilder();
        String defaultMsg;
        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError field = (FieldError) error;
                defaultMsg = devActive
                        ? StrUtil.format("{}{}: {}, ", field.getField(), field.getDefaultMessage(), field.getRejectedValue())
                        : StrUtil.format("{}: {}, ", field.getDefaultMessage(), field.getRejectedValue());
                desc.append(defaultMsg);
            } else {
                defaultMsg = devActive
                        ? StrUtil.format("{}{}: {}, ", error.getObjectName(), error.getDefaultMessage(), error.getArguments())
                        : StrUtil.format("{}: {}, ", error.getDefaultMessage(), error.getArguments());
                desc.append(defaultMsg);
            }
        }
        // 删除最后的", "部分.
        desc.delete(desc.length() - 2, desc.length());
        log.error("Argument Not Valid Exception: {}", desc);
        String message = SysStatus.INVALID_PARAM.getMessage() + desc;
        return BaseResult.error(SysStatus.INVALID_PARAM.getCode(), message);
    }

    @ExceptionHandler(MultipartException.class)
    public BaseResult handleMultipartException(MultipartException exception) {
        log.error("Multipart Exception: {}", exception.getMessage());
        return BaseResult.error(SysStatus.FILE_UPLOAD_ERROR);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseResult handleMaxUploadSizeExceeded(MaxUploadSizeExceededException exception) {
        log.error("Max Upload Size Exceeded Exception: {}", exception.getMessage());
        return BaseResult.error(SysStatus.UPLOAD_FILE_MAX_LIMIT);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResult handlerMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        log.error("Missing Parameter Exception: {}", exception.getParameterName());
        return BaseResult.error(SysStatus.MISSING_PARAM.getCode(), String.format(SysStatus.MISSING_PARAM.getMessage(), exception.getParameterName()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public BaseResult handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.error("Argument Type Mismatch Exception: {}", exception.getMessage());
        return BaseResult.error(SysStatus.INVALID_PARAM_TYPE.getCode(), String.format(SysStatus.INVALID_PARAM_TYPE.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResult handlerHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("Parameter Not Readable Exception: {}", exception.getMessage());
        return BaseResult.error(SysStatus.READ_PARAM_FAIL.getCode(), String.format(SysStatus.READ_PARAM_FAIL.getMessage(), exception.getMessage()));
    }

}

