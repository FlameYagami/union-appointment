package com.gk.common.enums;

/**
 * 分页类型
 *
 * @author Flame
 * @since 2025-06-17 17:07
 **/
public enum PageType {

    DEFAULT("1"),

    TOTAL("2"),

    DATA("3");

    public final String value;

    PageType(String value) {
        this.value = value;
    }

}
