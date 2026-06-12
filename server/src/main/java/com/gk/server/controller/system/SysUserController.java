package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.model.dto.LabelResp;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.common.utils.ExcelUtils;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.model.dto.system.sysUser.BlacklistStatusChangeReq;
import com.gk.framework.model.dto.system.sysUser.PasswordAlterReq;
import com.gk.framework.model.dto.system.sysUser.PasswordChangeReq;
import com.gk.framework.model.dto.system.sysUser.UserStatusChangeReq;
import com.gk.framework.service.intf.system.ISysBlacklistService;
import com.gk.framework.service.intf.system.ISysUserService;
import com.gk.server.model.dto.system.sysUser.*;
import com.gk.server.service.intf.system.ISysUserExtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 用户信息 控制类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */

@RestController
@RequestMapping("/api/sys-user")
@Api(tags = "用户信息管理")
@Validated
public class SysUserController {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysUserExtService sysUserExtService;
    @Resource
    private ISysBlacklistService sysBlacklistService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:sys-user:list')")
    @ApiOperation(value = "用户分页查询", notes = "server:sys-user:list")
    public PageResult<SysUserPageResp> pageList(SysUserPageReq req) {
        return PageResult.ok(sysUserExtService.pageList(req));
    }

    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermission('server:sys-user:export')")
    @ApiOperation(value = "用户列表导出", notes = "server:sys-user:export")
    @OperateLog(title = "用户列表导出", operateType = OperateType.EXPORT)
    public void exportList(HttpServletResponse response, @RequestBody SysUserExportReq req) {
        List<SysUserExportResp> dataList = sysUserExtService.exportList(req);
        ExcelUtils.write(response, "列表数据", SysUserExportResp.class, dataList);
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('server:sys-user:add')")
    @ApiOperation(value = "新增用户", notes = "server:sys-user:add")
    @OperateLog(title = "新增用户", operateType = OperateType.ADD)
    public ApiResult<Long> add(@RequestBody @Valid SysUserAddReq req) {
        long id = sysUserExtService.add(req);
        return ApiResult.ok(id);
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermission('server:sys-user:edit')")
    @ApiOperation(value = "修改用户", notes = "server:sys-user:edit")
    @OperateLog(title = "修改用户", operateType = OperateType.UPDATE)
    public ApiResult<?> edit(@RequestBody @Valid SysUserEditReq req) {
        sysUserExtService.edit(req);
        return ApiResult.ok();
    }

    @DeleteMapping
    @PreAuthorize("@ss.hasPermission('server:sys-user:delete')")
    @ApiOperation(value = "删除用户", notes = "server:sys-user:delete")
    @OperateLog(title = "删除用户", operateType = OperateType.DELETE)
    public ApiResult<?> delete(@RequestBody @Min(value = 1000000000000000002L, message = "id不合法") long id) {
        sysUserExtService.delete(id);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:sys-user:info')")
    @ApiOperation(value = "用户信息查询", notes = "server:sys-user:info")
    @ApiImplicitParam(name = "id", value = "用户信息id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<SysUserResp> findOne(@RequestParam("id") @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        return ApiResult.ok(sysUserExtService.findOne(id));
    }

    @PostMapping("/status/change")
    @PreAuthorize("@ss.hasPermission('server:sys-user:edit')")
    @ApiOperation(value = "修改用户状态", notes = "server:sys-user:edit")
    @OperateLog(title = "修改用户状态", operateType = OperateType.UPDATE)
    public ApiResult<?> changeDataStatus(@RequestBody @Valid UserStatusChangeReq req) {
        sysUserService.changeUserStatus(req);
        return ApiResult.ok();
    }

    @PostMapping("/password/reset")
    @PreAuthorize("@ss.hasPermission('server:sys-user:password-reset')")
    @ApiOperation(value = "用户密码重置", notes = "server:sys-user:password-reset")
    @OperateLog(title = "用户密码重置", operateType = OperateType.UPDATE)
    public ApiResult<?> resetPassword(@RequestBody @Min(value = 1000000000000000002L, message = "id不合法") long id) {
        return ApiResult.ok(sysUserService.resetPassword(id));
    }

    @PostMapping("/password/change")
    @PreAuthorize("@ss.hasPermission('server:sys-user:password-change')")
    @ApiOperation(value = "用户密码变更[通用权限]", notes = "server:sys-user:password-change")
    @OperateLog(title = "用户密码变更", operateType = OperateType.UPDATE)
    public ApiResult<?> changePassword(@RequestBody @Valid PasswordChangeReq req) {
        sysUserService.changePassword(req);
        return ApiResult.ok();
    }

    @PostMapping("/password/alter")
    @PreAuthorize("@ss.hasPermission('server:sys-user:password-alter')")
    @ApiOperation(value = "用户密码变更(重置密码后需要变更)[通用权限]", notes = "server:sys-user:password-alter")
    @OperateLog(title = "用户密码变更(重置密码后需要变更)", operateType = OperateType.UPDATE)
    public ApiResult<?> alterPassword(@RequestBody @Valid PasswordAlterReq req) {
        sysUserService.alterPassword(req);
        return ApiResult.ok();
    }

    @PostMapping("/unlock")
    @PreAuthorize("@ss.hasPermission('server:sys-user:unlock')")
    @ApiOperation(value = "账号解锁", notes = "server:sys-user:unlock")
    @OperateLog(title = "账号解锁", operateType = OperateType.UPDATE)
    public ApiResult<?> resetPassword(@RequestBody @NotBlank(message = "账号缺失") String username) {
        RedisCacheManager.getInstance().unlockAccount(username);
        return ApiResult.ok();
    }

    @GetMapping("/mgmt/role/label")
    @PreAuthorize("@ss.hasPermission('server:sys-user:list')")
    @ApiOperation(value = "角色标签查询(用于用户设置中的可选角色勾选)", notes = "server:sys-user:list")
    public ApiResult<List<LabelResp>> listMgmtRoleLabel() {
        return ApiResult.ok(sysUserService.listMgmtRoleLabel());
    }

    @PostMapping("/blacklist/change")
    @PreAuthorize("@ss.hasPermission('server:sys-user:blacklist-change')")
    @ApiOperation(value = "修改黑名单状态", notes = "server:sys-user:blacklist-change")
    @OperateLog(title = "修改黑名单状态", operateType = OperateType.UPDATE)
    public ApiResult<?> changeBlacklistStatus(@RequestBody @Valid BlacklistStatusChangeReq req) {
        sysBlacklistService.changeBlacklistStatus(req);
        return ApiResult.ok();
    }

}
