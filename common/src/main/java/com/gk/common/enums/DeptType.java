package com.gk.common.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * 部门类型
 *
 * @author Flame
 * @date 2023-02-21 16:05
 **/
@Getter
public enum DeptType implements EnumValuable {

    /**
     * 部门
     */
    DEPT("1"),

    /**
     * 科室
     */
    DEPARTMENT("2");

    public final String value;

    DeptType(String value) {
        this.value = value;
    }
}
