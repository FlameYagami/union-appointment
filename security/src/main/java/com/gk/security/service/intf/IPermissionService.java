package com.gk.security.service.intf;

import com.gk.security.model.dto.sysPermission.AuthMenuResp;
import com.gk.security.model.dto.sysPermission.AuthPermissionResp;
import com.gk.security.model.dto.sysPermission.DeptChangeReq;
import com.gk.security.model.dto.sysPermission.RoleChangeReq;

import java.util.List;

/**
 * 权限业务实接口
 *
 * @author Flame
 * @date 2023-02-02 11:28
 **/

public interface IPermissionService {

    void changeRole(RoleChangeReq req);

    void changeDept(DeptChangeReq req);

    boolean hasAnyPermissions(String[] permissions);

    boolean hasAnyRoles(String[] roles);

    AuthPermissionResp getAuthPermission();

    List<AuthMenuResp> getAuthMenu();

    boolean hasAnyOpenPermission(String[] permissions);

}
