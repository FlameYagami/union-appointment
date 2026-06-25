package com.gk.common.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统角色编码
 *
 * @author Flame
 * @since 2024-10-18 16:24
 **/
@Getter
@Slf4j
public enum SysRoleCode {

    /**
     * 超级管理员
     */
    SUPER_ADMIN("super-admin"),

    /**
     * 运维管理员
     */
    OPS_ADMIN("ops-admin"),

    /**
     * 系统管理员
     */
    SYSTEM_ADMIN("system-admin"),

    /**
     * 测试管理员
     */
    QA_TEST("qa-test"),

    /**
     * 活动资源管理员
     */
    ACTIVITY_ADMIN("activity-admin"),

    /**
     * 普通用户
     */
    STAFF("staff");

    public final String value;

    SysRoleCode(String value) {
        this.value = value;
    }

    /**
     * 判断角色编码是否为顶级管理员
     *
     * @param roleCode 角色编码
     */
    public static boolean isTopAdmin(String roleCode) {
        return SUPER_ADMIN.value.equals(roleCode) || OPS_ADMIN.value.equals(roleCode) || SYSTEM_ADMIN.value.equals(roleCode);
    }

    /**
     * 判断角色编码是否为己方顶级管理员
     *
     * @param roleCode 角色编码
     */
    public static boolean isSelfTopAdmin(String roleCode) {
        return SUPER_ADMIN.value.equals(roleCode) || OPS_ADMIN.value.equals(roleCode);
    }

}