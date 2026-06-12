package com.gk.common.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * 审核结果
 *
 * @author Flame
 * @since 2023-02-22 16:47
 **/
@Getter
public enum ReviewResult implements EnumValuable {

    /**
     * 通过
     */
    PASS("1"),

    /**
     * 驳回
     */
    REJECT("0");

    public final String value;

    ReviewResult(String value) {
        this.value = value;
    }

}
