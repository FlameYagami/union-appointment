package com.gk.framework.annotation;

import com.gk.common.enums.OperateType;
import com.gk.common.enums.OperatorType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 *
 * @author Flame
 * @date 2023-02-02 16:58
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {

    /**
     * 模块
     */
    String title() default "";

    /**
     * 操作类型
     */
    OperateType operateType() default OperateType.OTHER;

    /**
     * 操作人类型
     */
    OperatorType operatorType() default OperatorType.PLATFORM;

    /**
     * 是否记录操作日志
     */
    boolean enable() default true;

    /**
     * 是否记录方法参数
     */
    boolean logParam() default true;

    /**
     * 是否记录方法结果的数据
     */
    boolean logResult() default true;

}
