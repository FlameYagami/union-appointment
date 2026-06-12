package com.gk.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典转换注解 <br/>
 * 实现将字典值转换成字典文本标签
 *
 * @author GuoYu
 * @since 2023-02-22 09:50
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DictFormat {

    /**
     * 字典编码
     * 例如说，SysDictTypeConstants、InfDictTypeConstants
     */
    String value();

}
