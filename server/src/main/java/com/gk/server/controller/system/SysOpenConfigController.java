package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.model.dto.LabelResp;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.model.dto.system.base.DataStatusChangeReq;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigAddReq;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigEditReq;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigPageReq;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigPageResp;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigResp;
import com.gk.framework.service.intf.system.ISysOpenConfigService;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
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
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 第三方对接配置表 控制类
 *
 * @author Flame
 * @since 2023-03-13 16:25:50
 */

@RestController
@RequestMapping("/api/sys-open-config")
@Api(tags = "第三方对接配置管理")
@Validated
public class SysOpenConfigController {

    @Resource
    private ISysOpenConfigService sysOpenConfigService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "第三方对接配置分页查询")
    public PageResult<SysOpenConfigPageResp> pageList(SysOpenConfigPageReq req) {
        return PageResult.ok(sysOpenConfigService.pageList(req));
    }

    @PostMapping
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "新增第三方对接配置")
    @OperateLog(title = "新增第三方对接配置", operateType = OperateType.ADD)
    public ApiResult<Long> add(@RequestBody @Valid SysOpenConfigAddReq req) {
        long id = sysOpenConfigService.add(req);
        return ApiResult.ok(id);
    }

    @PutMapping
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "修改第三方对接配置")
    @OperateLog(title = "修改第三方对接配置", operateType = OperateType.UPDATE)
    public ApiResult<?> edit(@RequestBody @Valid SysOpenConfigEditReq req) {
        sysOpenConfigService.edit(req);
        return ApiResult.ok();
    }

    @DeleteMapping
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "删除第三方对接配置")
    @OperateLog(title = "删除第三方对接配置", operateType = OperateType.DELETE)
    public ApiResult<?> delete(@RequestBody @NotEmpty(message = "第三方对接配置id缺失") List<@Min(value = 1000000000000000001L, message = "id不合法") Long> idList) {
        sysOpenConfigService.delete(idList);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "第三方对接配置查询详情")
    @ApiImplicitParam(name = "id", value = "第三方对接配置id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<SysOpenConfigResp> findOne(@RequestParam("id") @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        return ApiResult.ok(sysOpenConfigService.findOne(id));
    }

    @PostMapping("/status/change")
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "修改秘钥状态")
    @OperateLog(title = "修改秘钥状态", operateType = OperateType.UPDATE)
    public ApiResult<?> changeDataStatus(@RequestBody @Valid DataStatusChangeReq req) {
        sysOpenConfigService.changeDataStatus(req);
        return ApiResult.ok();
    }

    @PostMapping("/key/generate")
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "生成秘钥")
    @OperateLog(title = "生成秘钥", operateType = OperateType.UPDATE)
    public ApiResult<?> generateKey(@RequestBody @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        sysOpenConfigService.generateKey(id);
        return ApiResult.ok();
    }

    @GetMapping("/mgmt/dept/tree-label")
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "部门树形结构标签查询(用于厂商设置中的可选部门勾选)")
    public ApiResult<List<TreeLabelResp>> listTreeLabel() {
        return ApiResult.ok(sysOpenConfigService.listMgmtDeptTreeLabel());
    }

    @GetMapping("/mgmt/role/label")
    @PreAuthorize("@ss.hasRole('super-admin')")
    @ApiOperation(value = "角色标签查询(用于厂商设置中的可选角色勾选)")
    public ApiResult<List<LabelResp>> listMgmtRoleLabel() {
        return ApiResult.ok(sysOpenConfigService.listMgmtRoleLabel());
    }

}
