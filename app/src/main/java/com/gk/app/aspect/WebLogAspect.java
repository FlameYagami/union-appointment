package com.gk.app.aspect;

import cn.hutool.core.util.StrUtil;
import com.gk.common.utils.AspectLogUtils;
import com.gk.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 网络请求日志切面
 *
 * @author Flame
 * @date 2023-03-03 10:39
 **/
@Aspect
@Component
@Slf4j
@Profile({"dev"})
public class WebLogAspect {

    /**
     * 是否打印请求url及参数
     */
    private final boolean needReq = true;

    /**
     * 是否打印请求响应结果
     */
    private final boolean needResp = false;

    @Pointcut("execution(public * com.gk.*.controller..*.*(..))")
    public void applicationWebLog() {

    }

    @Pointcut("applicationWebLog()")
    public void webLog() {

    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        if (needReq) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }
            HttpServletRequest request = attributes.getRequest();
            String req = StrUtil.format("[{}]: {}, Args => {}", request.getMethod(), request.getRequestURL().toString(), AspectLogUtils.obtainMethodParam(joinPoint));
            print(req);
        }
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        if (needResp) {
            String resp = StrUtil.format("Response => {}", JsonUtils.toJson(result));
            print(resp);
        }
        return result;
    }

    /**
     * 打印内容
     * @param content 文本内容
     */
    private synchronized void print(String content) {
        String line = StrUtil.fixLength("", '═', content.length() + 2);
        log.info("╔" + line + "╗");
        log.info("║ " + content + " ║");
        log.info("╚" + line + "╝");
    }

}
