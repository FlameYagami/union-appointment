package com.gk.framework.annotation;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 *
 * @author Flame
 * @date 2023-02-02 16:58
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 业务表的别名
     */
    String bizTableAlias() default "";

    /**
     * 部门id的别名
     */
    String deptIdAlias() default "dept_id";

    /**
     * 用户id的别名
     */
    String userIdAlias() default "create_by";


}
