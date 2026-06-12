package com.gk.common.constant;

/**
 * 时间常量类
 *
 * @author GuoYu
 * @since 2022-12-02 09:29
 **/
public class DateConstant {

    private DateConstant() {
        throw new IllegalStateException("Constant class");
    }

    /**
     * 默认格式
     */
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 日期格式
     */
    public static final String SIMPLE_DATE_PATTERN = "yyyyMMdd";

    /**
     * 时间格式
     */
    public static final String TIME_PATTERN = "HH:mm:ss";

    /**
     * 时间格式
     */
    public static final String HOUR_MINUTE_PATTERN = "HH:mm";

    /**
     * 中文默认格式
     */
    public static final String CN_DEFAULT_PATTERN = "yyyy年MM月dd日 HH时mm分ss秒";

    /**
     * 中文日期格式
     */
    public static final String CN_DATE_PATTERN = "yyyy年MM月dd日";

    /**
     * 中文时间格式
     */
    public static final String CN_TIME_PATTERN = "HH时mm分ss秒";

    /**
     * 默认时区
     */
    public static final String DEFAULT_TIME_ZONE = "GMT+8";
}
