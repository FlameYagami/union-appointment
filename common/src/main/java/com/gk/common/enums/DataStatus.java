package com.gk.common.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * 数据状态
 *
 * @author Flame
 * @date 2023-02-24 15:12
 **/
@Getter
public enum DataStatus implements EnumValuable {

    /**
     * 正常
     */
    NORMAL("1"),

    /**
     * 停用
     */
    DISABLE("0");

    public final String value;

    DataStatus(String value) {
        this.value = value;
    }

}
