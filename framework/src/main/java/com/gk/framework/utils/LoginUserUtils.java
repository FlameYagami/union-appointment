package com.gk.framework.utils;

import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.SysRoleCode;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.manager.RoleCacheManager;
import com.gk.framework.model.bo.security.LoginUser;
import com.gk.framework.model.entity.system.SysRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 登录用户工具类
 *
 * @author GuoYu
 * @since 2023-01-13 15:56
 */
@Slf4j
public class LoginUserUtils {

    private LoginUserUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取用户id
     **/
    public static long getUserId() {
        try {
            return getLoginUser().getUserId();
        } catch (Exception e) {
            throw new SysException(SysStatus.UNAUTHORIZED_USER_ID);
        }
    }

    /**
     * 获取用户id或者null
     */
    public static Long getUserIdOrNull() {
        try {
            return getLoginUser().getUserId();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new SysException(SysStatus.UNAUTHORIZED_USER_INFO);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 判定当前用户是否为超管
     */
    public static boolean isSuperAdmin() {
        return CommonConstant.TOP_ID == getUserId();
    }

    /**
     * 判定当前用户是否为顶级管理员
     */
    public static boolean isTopAdmin() {
        return SysRoleCode.isTopAdmin(getCurrentRoleCode());
    }

    /**
     * 获取当前的角色
     */
    public static String getCurrentRoleCode() {
        long roleId = RedisCacheManager.getInstance().getRedisRoleId();
        if (roleId == CommonConstant.NA_VALUE) {
            log.error("Get Role Code Error: current role id is not found");
            return CommonConstant.EMPTY;
        }
        SysRole sysRole = RoleCacheManager.getInstance().findByRoleId(roleId);
        if (sysRole == null) {
            log.error("Get Role Code Error: current sys role is not found");
            return CommonConstant.EMPTY;
        }
        return sysRole.getRoleCode();
    }

}
