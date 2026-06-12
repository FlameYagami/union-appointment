package com.gk.framework.interceptor;

import com.gk.common.model.others.BaseResult;
import com.gk.common.utils.ServletExtUtils;
import com.gk.framework.annotation.RepeatSubmit;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

/**
 * 防止重复提交拦截器
 *
 * @author Flame
 * @since 2022-11-20 10:16
 */
@Component
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null && this.isRepeatSubmit(request, annotation)) {
                BaseResult resp = BaseResult.error(annotation.message());
                ServletExtUtils.responseJson(response, resp);
                return false;
            }
        }
        return true;
    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation);

}
