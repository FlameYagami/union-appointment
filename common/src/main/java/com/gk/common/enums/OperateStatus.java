package com.gk.common.enums;

/**
 * 操作状态
 *
 * @author Kevin
 */
public enum OperateStatus {

    /**
     * 成功
     */
    SUCCESS("1"),

    /**
     * 失败
     */
    FAILED("0");

    public final String value;

    OperateStatus(String value) {
        this.value = value;
    }

}
