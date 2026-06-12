package com.gk.framework.validate;

import cn.hutool.core.util.StrUtil;
import com.gk.common.intf.EnumValuable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 枚举类型的校验器
 *
 * @author GuoYu
 * @since 2023-02-22 16:21
 */
public class InEnumValidator implements ConstraintValidator<InEnum, String> {

    private List<String> values;

    @Override
    public void initialize(InEnum annotation) {
        EnumValuable[] enumValues = annotation.value().getEnumConstants();
        if (enumValues.length == 0) {
            this.values = Collections.emptyList();
        } else {
            this.values = Arrays.stream(enumValues).map(EnumValuable::getValue).collect(Collectors.toList());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isEmpty(value)) {
            return true;
        }
        // 校验通过
        if (values.contains(value)) {
            return true;
        }
        // 校验不通过，自定义提示语句（因为，注解上的 value 是枚举类，无法获得枚举类的实际值）
        // 禁用默认的 message 的值
        context.disableDefaultConstraintViolation();
        // 重新添加错误提示语句
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addConstraintViolation();
        return false;
    }

}

