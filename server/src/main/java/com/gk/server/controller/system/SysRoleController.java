package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.manager.RoleCacheManager;
import com.gk.framework.model.dto.system.base.DataStatusChangeReq;
import com.gk.framework.model.dto.system.sysRole.*;
import com.gk.framework.service.intf.system.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 角色表 控制类
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */

@RestController
@RequestMapping("/api/sys-role")
@Api(tags = "角色管理")
@Validated
public class SysRoleController {

    @Resource
    private ISysRoleService sysRoleService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:sys-role:list')")
    @ApiOperation(value = "角色分页查询", notes = "server:sys-role:list")
    public PageResult<SysRolePageResp> pageList(SysRolePageReq req) {
        return PageResult.ok(sysRoleService.pageList(req));
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('server:sys-role:add')")
    @ApiOperation(value = "新增角色", notes = "server:sys-role:add")
    @OperateLog(title = "新增角色", operateType = OperateType.ADD)
    public ApiResult<Long> add(@RequestBody @Valid SysRoleAddReq req) {
        long id = sysRoleService.add(req);
        return ApiResult.ok(id);
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermission('server:sys-role:edit')")
    @ApiOperation(value = "修改角色", notes = "server:sys-role:edit")
    @OperateLog(title = "修改角色", operateType = OperateType.UPDATE)
    public ApiResult<?> edit(@RequestBody @Valid SysRoleEditReq req) {
        sysRoleService.edit(req);
        return ApiResult.ok();
    }

    @DeleteMapping
    @PreAuthorize("@ss.hasPermission('server:sys-role:delete')")
    @ApiOperation(value = "删除角色", notes = "server:sys-role:delete")
    @OperateLog(title = "删除角色", operateType = OperateType.DELETE)
    public ApiResult<?> delete(@RequestBody @Min(value = 1000000000000000002L, message = "id不合法") long id) {
        sysRoleService.delete(id);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:sys-role:info')")
    @ApiOperation(value = "角色查询详情", notes = "server:sys-role:info")
    @ApiImplicitParam(name = "id", value = "角色id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<SysRoleResp> findOne(@RequestParam("id") @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        return ApiResult.ok(sysRoleService.findOne(id));
    }

    @GetMapping("/mgmt/menu/tree-label")
    @PreAuthorize("@ss.hasPermission('server:sys-role:list')")
    @ApiOperation(value = "菜单树形结构查询(用于角色设置中的可选菜单勾选)", notes = "server:sys-role:list")
    public ApiResult<List<TreeLabelResp>> listMgmtMenuTreeLabel() {
        return ApiResult.ok(sysRoleService.listMgmtMenuTreeLabel());
    }

    @PostMapping("/status/change")
    @PreAuthorize("@ss.hasPermission('server:sys-role:edit')")
    @ApiOperation(value = "修改角色状态", notes = "server:sys-role:edit")
    @OperateLog(title = "修改角色状态", operateType = OperateType.UPDATE)
    public ApiResult<?> changeDataStatus(@RequestBody @Valid DataStatusChangeReq req) {
        sysRoleService.changeDataStatus(req);
        return ApiResult.ok();
    }

    @PostMapping("/refresh-cache")
    @PreAuthorize("@ss.hasPermission('server:sys-role:refresh-cache')")
    @ApiOperation(value = "刷新角色缓存", notes = "server:sys-role:refresh-cache")
    public ApiResult<?> refreshCache() {
        RoleCacheManager.getInstance().refreshCache();
        return ApiResult.ok();
    }

}
