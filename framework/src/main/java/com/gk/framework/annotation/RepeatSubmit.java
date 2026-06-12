package com.gk.framework.annotation;

import java.lang.annotation.*;

/**
 * 防止表单重复提交注解
 *
 * @author Flame
 * @date 2023-02-02 16:58
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {

    /**
     * 间隔时间(ms)，小于此时间视为重复提交
     */
    int interval() default 5000;

    /**
     * 提示消息
     */
    String message() default "不允许重复提交，请稍候再试";

}
