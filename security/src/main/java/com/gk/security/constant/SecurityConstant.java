package com.gk.security.constant;

/**
 * 认证鉴权常量类
 *
 * @author GuoYu
 * @since 2023-01-13 16:11
 **/
public class SecurityConstant {

    private SecurityConstant() {
        throw new IllegalStateException("Constant class");
    }

    /**
     * 1秒(单位:毫秒)
     */
    public static final long MILLIS_SECOND = 1000;
    /**
     * 1分钟(单位:毫秒)
     */
    public static final long MILLIS_MINUTE =  MILLIS_SECOND * 60;
    /**
     * 10分钟(单位:毫秒)
     */
    public static final long MILLIS_TEN_MINUTE = 10 * MILLIS_MINUTE;
    /**
     * 1天(单位:毫秒)
     */
    public static final long MILLIS_ONE_DAY = 24 * 60 * MILLIS_MINUTE;
}
