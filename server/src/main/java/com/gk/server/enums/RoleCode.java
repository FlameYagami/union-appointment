package com.gk.server.enums;

import com.gk.common.enums.SysRoleCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 角色编码{@link SysRoleCode}
 *
 * @author Flame
 * @since 2024-10-18 16:24
 **/
@Getter
@Slf4j
public enum RoleCode {

    /**
     * 超级管理员
     */
    SUPER_ADMIN(SysRoleCode.SUPER_ADMIN),

    /**
     * 运维管理员
     */
    OPS_ADMIN(SysRoleCode.OPS_ADMIN),

    /**
     * 系统管理员
     */
    SYSTEM_ADMIN(SysRoleCode.SYSTEM_ADMIN),

    /**
     * 活动资源管理员
     */
    ACTIVITY_ADMIN(SysRoleCode.ACTIVITY_ADMIN),

    /**
     * 普通用户
     */
    STAFF(SysRoleCode.STAFF);

    /**
     * 其他角色编码根据业务需求自行添加
     */

    public final String value;

    RoleCode(String value) {
        this.value = value;
    }

    RoleCode(SysRoleCode sysRoleCode) {
        this.value = sysRoleCode.getValue();
    }

}