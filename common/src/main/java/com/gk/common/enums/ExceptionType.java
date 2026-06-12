package com.gk.common.enums;

import lombok.Getter;

/**
 * 异常类型
 *
 * @author Flame
 * @since 2024-04-09 15:11
 **/
@Getter
public enum ExceptionType {

    /**
     * 暴力登录
     */
    BRUTE_FORCE_LOGIN("1"),

    /**
     * 异地登录
     */
    REMOTE_LOGIN("2"),

    /**
     * 账号锁定
     */
    ACCOUNT_LOCKED("3"),

    /**
     * 密码更改
     */
    PASSWORD_CHANGED("4");

    public final String value;

    ExceptionType(String value) {
        this.value = value;
    }

}
