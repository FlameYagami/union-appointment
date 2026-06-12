package com.gk.server.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * 注册用户类型
 *
 * @author Flame
 * @since 2023-02-22 16:47
 **/
@Getter
public enum RegisterUserType implements EnumValuable {

    /**
     * 普通用户
     */
    STAFF(SysUserType.STAFF);

    /**
     * 其他用户类型根据业务需求自行添加
     */

    public final String value;

    RegisterUserType(String value) {
        this.value = value;
    }

    RegisterUserType(SysUserType type) {
        this.value = type.value;
    }

}
