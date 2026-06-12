package com.gk.server.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * 系统用户类型
 *
 * @author Flame
 * @since 2023-02-22 16:47
 **/
@Getter
public enum SysUserType implements EnumValuable {

    /**
     * 管理员
     */
    ADMIN("1"),

    /**
     * 普通用户
     */
    STAFF("2");

    /**
     * 其他用户类型根据业务需求自行修改
     */

    public final String value;

    SysUserType(String value) {
        this.value = value;
    }

    public static boolean isStaff(String userType) {
        return STAFF.value.equals(userType);
    }

}
