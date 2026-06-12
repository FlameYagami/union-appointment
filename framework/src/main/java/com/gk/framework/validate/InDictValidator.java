package com.gk.framework.validate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.framework.manager.DictCacheManager;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.List;

/**
 * 字典数据的校验器
 *
 * @author GuoYu
 * @since 2023-02-22 16:21
 */
@Slf4j
public class InDictValidator implements ConstraintValidator<InDict, String> {

    private String dictCode;

    @Override
    public void initialize(InDict annotation) {
        this.dictCode = annotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isEmpty(value)) {
            return true;
        }
        List<String> dictValues = Collections.emptyList();
        if (StrUtil.isNotEmpty(dictCode)) {
            dictValues = DictCacheManager.getDictAllValues(dictCode);
            if (CollUtil.isEmpty(dictValues)) {
                log.error("InDictValidator Valid Error: {} 对应的字典项不存在", dictCode);
            }
        }
        // 校验通过
        List<String> valueList = StrUtil.split(value, CommonConstant.COMMA, true, true);
        if (CollUtil.containsAll(dictValues, valueList)) {
            return true;
        }
        // 校验不通过
        // 禁用默认的 message 的值
        context.disableDefaultConstraintViolation();
        // 重新添加错误提示语句
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addConstraintViolation();
        return false;
    }

}

