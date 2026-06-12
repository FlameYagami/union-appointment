package com.gk.security.handler;

import com.gk.common.enums.SysStatus;
import com.gk.common.model.others.BaseResult;
import com.gk.common.utils.ServletExtUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author Flame
 * @since 2022-11-20 10:16
 */

@Component
public class AuthenticationHandler implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        ServletExtUtils.responseJson(response, BaseResult.error(SysStatus.UNAUTHORIZED_ERROR));
    }

}
