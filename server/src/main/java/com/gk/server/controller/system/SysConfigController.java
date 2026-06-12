package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.common.utils.ExcelUtils;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.model.dto.system.sysConfig.*;
import com.gk.framework.service.intf.system.IRedisCacheService;
import com.gk.framework.service.intf.system.ISysConfigService;
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
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 配置表 控制类
 *
 * @author GuoYu
 * @since 2023-03-06 14:56:24
 */

@RestController
@RequestMapping("/api/sys-config")
@Api(tags = "配置管理")
@Validated
public class SysConfigController {

    @Resource
    private ISysConfigService sysConfigService;

    @Resource
    private IRedisCacheService redisCacheService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:sys-config:list')")
    @ApiOperation(value = "配置分页查询", notes = "server:sys-config:list")
    public PageResult<SysConfigPageResp> pageList(SysConfigPageReq req) {
        return PageResult.ok(sysConfigService.pageList(req));
    }

    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermission('server:sys-config:export')")
    @ApiOperation(value = "配置列表导出", notes = "server:sys-config:export")
    @OperateLog(title = "配置列表导出", operateType = OperateType.EXPORT)
    public void exportList(HttpServletResponse response, @RequestBody SysConfigExportReq req) {
        List<SysConfigExportResp> dataList = sysConfigService.exportList(req);
        ExcelUtils.write(response, "列表数据", SysConfigExportResp.class, dataList);
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('server:sys-config:add')")
    @ApiOperation(value = "新增配置", notes = "server:sys-config:add")
    @OperateLog(title = "新增配置", operateType = OperateType.ADD)
    public ApiResult<Long> add(@RequestBody @Valid SysConfigAddReq req) {
        long id = sysConfigService.add(req);
        return ApiResult.ok(id);
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermission('server:sys-config:edit')")
    @ApiOperation(value = "修改配置", notes = "server:sys-config:edit")
    @OperateLog(title = "修改配置", operateType = OperateType.UPDATE)
    public ApiResult<?> edit(@RequestBody @Valid SysConfigEditReq req) {
        sysConfigService.edit(req);
        return ApiResult.ok();
    }

    @DeleteMapping
    @PreAuthorize("@ss.hasPermission('server:sys-config:delete')")
    @ApiOperation(value = "删除配置", notes = "server:sys-config:delete")
    @OperateLog(title = "删除配置", operateType = OperateType.DELETE)
    public ApiResult<?> delete(@RequestBody @NotEmpty(message = "配置id缺失") List<@Min(value = 1000000000000000001L, message = "id不合法") Long> idList) {
        sysConfigService.delete(idList);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:sys-config:info')")
    @ApiOperation(value = "配置查询详情", notes = "server:sys-config:info")
    @ApiImplicitParam(name = "id", value = "配置id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<SysConfigResp> findOne(@RequestParam("id") @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        return ApiResult.ok(sysConfigService.findOne(id));
    }

    @GetMapping("/value")
    @PreAuthorize("@ss.hasPermission('server:sys-config:value')")
    @ApiOperation(value = "查询配置值", notes = "server:sys-config:value")
    @ApiImplicitParam(name = "configKey", value = "配置键", required = true, dataTypeClass = String.class, example = "config-key")
    public ApiResult<String> findConfigValue(@RequestParam("configKey") @NotBlank(message = "配置键缺失") String configKey) {
        return ApiResult.ok(redisCacheService.getConfigValue(configKey));
    }

    @GetMapping("/values")
    @PreAuthorize("@ss.hasPermission('server:sys-config:value')")
    @ApiOperation(value = "查询配置值列表", notes = "server:sys-config:value")
    @ApiImplicitParam(name = "configKeys", value = "配置键", required = true, dataTypeClass = String.class, example = "configValue1, configValue2")
    public ApiResult<List<SysConfigPairResp>> listConfigPair(@RequestParam("configKeys") @NotEmpty(message = "配置键缺失") List<@NotBlank(message = "配置键缺失") String> configKeys) {
        return ApiResult.ok(sysConfigService.listConfigPair(configKeys));
    }

    @GetMapping("/refresh-cache")
    @PreAuthorize("@ss.hasPermission('server:sys-config:refresh-cache')")
    @ApiOperation(value = "配置刷新缓存", notes = "server:sys-config:refresh-cache")
    public ApiResult<?> refreshCache() {
        sysConfigService.refreshCache(null);
        return ApiResult.ok();
    }

}
