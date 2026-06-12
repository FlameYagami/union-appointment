package com.gk.common.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * 可用和不可用的通用枚举(只适用数据库的Enabled字段)
 *
 * @author Flame
 * @date 2023-02-15 10:38
 **/
@Getter
public enum EnabledType implements EnumValuable {

    /**
     * 可用
     */
    ENABLE("1"),

    /**
     * 不可用
     */
    DISABLE("0");

    public final String value;

    EnabledType(String value) {
        this.value = value;
    }
}
