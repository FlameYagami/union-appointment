package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.manager.MenuCacheManager;
import com.gk.framework.model.dto.system.sysMenu.SysMenuAddReq;
import com.gk.framework.model.dto.system.sysMenu.SysMenuEditReq;
import com.gk.framework.model.dto.system.sysMenu.SysMenuResp;
import com.gk.framework.model.dto.system.sysMenu.SysMenuTreeReq;
import com.gk.framework.model.dto.system.sysMenu.SysMenuTreeResp;
import com.gk.framework.service.intf.system.ISysMenuService;
import com.gk.common.model.others.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 菜单权限表 控制类
 *
 * @author Flame
 * @since 2022-12-29 09:30:38
 */

@RestController
@RequestMapping("/api/sys-menu")
@Api(tags = "菜单权限管理")
@Validated
public class SysMenuController {

    @Resource
    private ISysMenuService sysMenuService;

    @GetMapping("/tree")
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "菜单权限树形结构查询")
    public ApiResult<List<SysMenuTreeResp>> listTree(SysMenuTreeReq req) {
        return ApiResult.ok(sysMenuService.listTree(req));
    }

    @PostMapping
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "新增菜单权限")
    @OperateLog(title = "新增菜单权限", operateType = OperateType.ADD)
    public ApiResult<?> add(@RequestBody @Valid SysMenuAddReq req) {
        sysMenuService.add(req);
        return ApiResult.ok();
    }

    @PutMapping
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "修改菜单权限")
    @OperateLog(title = "修改菜单权限", operateType = OperateType.UPDATE)
    public ApiResult<?> edit(@RequestBody @Valid SysMenuEditReq req) {
        sysMenuService.edit(req);
        return ApiResult.ok();
    }

    @DeleteMapping
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "删除菜单权限")
    @OperateLog(title = "删除菜单权限", operateType = OperateType.DELETE)
    public ApiResult<?> delete(@RequestBody @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        sysMenuService.delete(id);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "菜单权限详情查询")
    @ApiImplicitParam(name = "id", value = "菜单权限id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<SysMenuResp> findOne(@RequestParam("id") @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        return ApiResult.ok(sysMenuService.findOne(id));
    }

    @GetMapping("/tree-label")
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "菜单树形结构标签查询")
    public ApiResult<List<TreeLabelResp>> listTreeLabel() {
        return ApiResult.ok(sysMenuService.listTreeLabel());
    }

    @PostMapping("/refresh-cache")
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "刷新菜单缓存")
    public ApiResult<?> refreshCache() {
        MenuCacheManager.getInstance().refreshCache();
        return ApiResult.ok();
    }

}
