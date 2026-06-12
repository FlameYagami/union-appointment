package com.gk.security.service.intf;

/**
 * 权限框架接口类
 *
 * @author Flame
 * @date 2023-02-02 11:28
 **/
public interface IAuthPermissionService {

    boolean hasPermission(String permission);

    boolean hasAnyPermissions(String... permissions);

    boolean hasRole(String role);

    boolean hasAnyRoles(String... roles);

    boolean hasOpenPermission(String permission);

    boolean hasAnyOpenPermissions(String... permission);
}
