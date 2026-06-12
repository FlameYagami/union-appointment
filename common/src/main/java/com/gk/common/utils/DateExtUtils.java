package com.gk.common.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import com.gk.common.constant.DateConstant;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间 扩展工具类
 *
 * @author Flame
 * @since 2025-02-27 9:10
 **/
public class DateExtUtils {

    private DateExtUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 格式化为 时间格式
     */
    public static String formatTime(Date date) {
        return DateUtil.format(date, DateConstant.DEFAULT_PATTERN);
    }

    /**
     * 格式化为 日期格式
     */
    public static String formatDate(Date date) {
        return DateUtil.format(date, DateConstant.DATE_PATTERN);
    }

    /**
     * 格式化为 中文日期格式
     */
    public static String formatCNDate(Date date) {
        return DateUtil.format(date, DateConstant.CN_DATE_PATTERN);
    }

    /**
     * 格式化为 时分格式
     */
    public static String formatHourMinute(LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ofPattern(DateConstant.HOUR_MINUTE_PATTERN));
    }

    /**
     * 处理查询年龄参数转换为生日日期字符串
     *
     * @param startAge 起始年龄
     * @param endAge   截止年龄
     * @return 键值对对象, key: 开始生日日期, value: 结束生日日期
     */
    public static Pair<String, String> ageToBirth(int startAge, int endAge) {
        Date nowDate = new Date();
        // 截止年龄要扩大1岁
        int trueEndAge = endAge + 1;
        Date startDate = DateUtil.offset(nowDate, DateField.YEAR, -trueEndAge);
        // 开始日期要减少1天
        Date tempEndDate = DateUtil.offset(nowDate, DateField.YEAR, -startAge);
        Date endDate = DateUtil.offsetDay(tempEndDate, -1);

        String startBirth = DateUtil.format(startDate, DateConstant.DATE_PATTERN);
        String endBirth = DateUtil.format(endDate, DateConstant.DATE_PATTERN);
        return new Pair<>(startBirth, endBirth);
    }
}
