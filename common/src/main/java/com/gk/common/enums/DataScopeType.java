package com.gk.common.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * 数据权限类型
 *
 * @author Flame
 * @date 2023-02-02 17:02
 **/
@Getter
public enum DataScopeType implements EnumValuable {

    /**
     * 全平台数据
     */
    ALL("1"),

    /**
     * 部门及以下数据
     */
    DEPT_AND_CHILD("2"),

    /**
     * 本部门数据
     */
    DEPT("3"),

    /**
     * 个人数据
     */
    SELF("4");

    public final String value;

    DataScopeType(String value) {
        this.value = value;
    }
}
