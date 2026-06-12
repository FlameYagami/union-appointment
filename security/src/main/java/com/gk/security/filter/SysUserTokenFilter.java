package com.gk.security.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.gk.framework.model.bo.security.LoginUser;
import com.gk.framework.utils.TokenUtils;
import com.gk.security.model.bo.SecurityParam;
import com.gk.security.service.intf.IAuthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * token过滤器 验证token有效性
 *
 * @author Flame
 * @since 2022-11-20 10:16
 */

@Component
@Order(1)
@Slf4j
public class SysUserTokenFilter extends OncePerRequestFilter {

    @Resource
    private IAuthTokenService authTokenService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 匿名接口不进行拦截
        String requestUri = request.getRequestURI();
        SecurityParam securityParam = SpringUtil.getBean(SecurityParam.class);
        // 使用AntPathMatcher做正则匹配
        AntPathMatcher antMatcher = new AntPathMatcher();
        return Stream.of(securityParam.getAnonymous()).anyMatch(it -> antMatcher.match(it, requestUri));
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws ServletException, IOException {
        String token = TokenUtils.getRequestToken(request);
        if (StrUtil.isNotEmpty(token)) {
            authTokenService.verifyToken(request);
            LoginUser loginUser = authTokenService.findLoginUser(request);
            if (loginUser != null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } else {
            log.debug("System Token Filter: token is empty");
        }
        chain.doFilter(request, response);
    }

}
