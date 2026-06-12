package com.gk.common.utils;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * SpEL表达式工具类
 *
 * @author GuoYu
 * @since 2023-06-01 09:05
 **/
@Slf4j
public class ExpressionUtils {

    private ExpressionUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 解析SpEL表达式
     *
     * @param expStr 表达式字符串
     * @param params 参数Map
     * @param tClass 返回值泛型
     */
    public static <T> T getExpressionValue(String expStr, Map<String, Object> params, Class<T> tClass) {
        ExpressionParser expParser = new SpelExpressionParser();
        Expression expression = expParser.parseExpression(expStr);
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (MapUtil.isNotEmpty(params)) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        }

        try {
            final T result = expression.getValue(context, tClass);
            if (result != null) {
                return result;
            }
        } catch (EvaluationException e) {
            log.error("Expression get value error: expStr = {}, params = {}", expStr, params);
        }

        return null;
    }
}
