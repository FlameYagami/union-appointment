package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.model.dto.system.sysUserRole.*;
import com.gk.framework.service.intf.system.ISysUserRoleService;
import com.gk.framework.service.intf.system.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户角色表 控制类
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */

@RestController
@RequestMapping("/api/sys-user-role")
@Api(tags = "用户角色管理")
@Validated
public class SysUserRoleController {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysUserRoleService sysUserRoleService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:sys-user-role:page')")
    @ApiOperation(value = "角色中用户分页查询", notes = "server:sys-user-role:page")
    public PageResult<SysUserRolePageResp> pageList(SysUserRolePageReq req) {
        return PageResult.ok(sysUserRoleService.pageUserList(req));
    }

    @GetMapping("/user/page")
    @PreAuthorize("@ss.hasPermission('server:sys-user-role:user-page')")
    @ApiOperation(value = "用户添加分页查询", notes = "server:sys-user-role:user-page")
    public PageResult<SysUserRoleAddPageResp> pageUserList(SysUserRoleAddPageReq req) {
        return PageResult.ok(sysUserRoleService.pageUserAddList(req));
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('server:sys-user-role:add')")
    @ApiOperation(value = "新增角色中用户", notes = "server:sys-user-role:add")
    @OperateLog(title = "新增角色中用户", operateType = OperateType.ADD)
    public ApiResult<?> add(@RequestBody @Valid SysUserRoleAddReq req) {
        sysUserRoleService.addUser(req);
        return ApiResult.ok();
    }

    @DeleteMapping
    @PreAuthorize("@ss.hasPermission('server:sys-user-role:delete')")
    @ApiOperation(value = "删除角色中用户", notes = "server:sys-user-role:delete")
    @OperateLog(title = "删除角色中用户", operateType = OperateType.DELETE)
    public ApiResult<?> delete(@RequestBody @Valid SysUserRoleDeleteReq req) {
        sysUserRoleService.deleteUser(req);
        return ApiResult.ok();
    }

}
