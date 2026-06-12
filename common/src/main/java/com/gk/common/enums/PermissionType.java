package com.gk.common.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * 权限类型
 *
 * @author Flame
 * @date 2022/11/30 14:24
 */
@Getter
public enum PermissionType implements EnumValuable {

    /**
     * 超级管理员
     */
    SUPER_ADMIN("1"),

    /**
     * 普通管理员
     */
    NORMAL_ADMIN("2");

    public final String value;

    PermissionType(String value) {
        this.value = value;
    }

}
