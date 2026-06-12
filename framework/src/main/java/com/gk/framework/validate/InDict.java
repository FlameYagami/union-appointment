package com.gk.framework.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典数据校验注解
 *
 * @author GuoYu
 * @since 2023-02-23 15:33
 **/

@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = InDictValidator.class)
public @interface InDict {

    /**
     * 字典编码
     */
    String value();

    String message() default "参数超出指定范围";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
