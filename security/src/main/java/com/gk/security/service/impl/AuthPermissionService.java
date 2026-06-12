package com.gk.security.service.impl;

import com.gk.security.service.intf.IAuthPermissionService;
import com.gk.security.service.intf.IPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 权限框架实现类
 *
 * @author Flame
 * @date 2023-02-02 11:28
 **/

@Service("ss")
public class AuthPermissionService implements IAuthPermissionService {

    @Resource
    private IPermissionService permissionService;

    /**
     * 判断是否有权限
     */
    @Override
    public boolean hasPermission(String permission) {
        return hasAnyPermissions(permission);
    }

    /**
     * 判断是否有权限，任一一个即可
     */
    @Override
    public boolean hasAnyPermissions(String... permissions) {
        return permissionService.hasAnyPermissions(permissions);
    }

    /**
     * 判断是否有角色
     */
    @Override
    public boolean hasRole(String role) {
        return hasAnyRoles(role);
    }

    /**
     * 判断是否有角色，任一一个即可
     */
    @Override
    public boolean hasAnyRoles(String... roles) {
        return permissionService.hasAnyRoles(roles);
    }

    /**
     * 判断是否有权限(第三方对接权限)
     */
    @Override
    public boolean hasOpenPermission(String permission) {
        return hasAnyOpenPermissions(permission);
    }

    /**
     * 判断是否有权限，任一一个即可(第三方对接权限)
     */
    @Override
    public boolean hasAnyOpenPermissions(String... permission) {
        return permissionService.hasAnyOpenPermission(permission);
    }

}
