package com.gk.common.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * 是或否的通用枚举
 *
 * @author Flame
 * @date 2023-02-15 10:38
 **/
@Getter
public enum YesOrNo implements EnumValuable {

    /**
     * 是
     */
    YES("1"),

    /**
     * 否
     */
    NO("0");

    public final String value;

    YesOrNo(String value) {
        this.value = value;
    }

}
