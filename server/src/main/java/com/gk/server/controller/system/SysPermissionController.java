package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.framework.annotation.OperateLog;
import com.gk.security.model.dto.sysPermission.DeptChangeReq;
import com.gk.security.model.dto.sysPermission.AuthPermissionResp;
import com.gk.security.model.dto.sysPermission.RoleChangeReq;
import com.gk.security.model.dto.sysPermission.AuthMenuResp;
import com.gk.security.service.intf.IPermissionService;
import com.gk.common.model.others.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 权限 控制类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */

@RestController
@RequestMapping("/api/sys-permission")
@Api(tags = "权限管理")
@Validated
public class SysPermissionController {

    @Resource
    private IPermissionService permissionService;

    @GetMapping("/permission")
    @ApiOperation(value = "权限信息")
    public ApiResult<AuthPermissionResp> getAuthPermission() {
        return ApiResult.ok(permissionService.getAuthPermission());
    }

    @GetMapping("/menu")
    @ApiOperation(value = "菜单信息")
    public ApiResult<List<AuthMenuResp>> getAuthMenu() {
        return ApiResult.ok(permissionService.getAuthMenu());
    }

    @PostMapping("/role/change")
    @ApiOperation(value = "切换角色")
    @OperateLog(title = "切换角色", operateType = OperateType.OTHER)
    public ApiResult<?> changeRole(@RequestBody @Valid RoleChangeReq req) {
        permissionService.changeRole(req);
        return ApiResult.ok();
    }

    @PostMapping("/dept/change")
    @ApiOperation(value = "切换部门")
    @OperateLog(title = "切换部门", operateType = OperateType.OTHER)
    public ApiResult<?> changeDept(@RequestBody @Valid DeptChangeReq req) {
        permissionService.changeDept(req);
        return ApiResult.ok();
    }

}
