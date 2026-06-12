package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.common.utils.ExcelUtils;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.model.dto.system.sysDict.*;
import com.gk.framework.service.intf.system.ISysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 字典表 控制类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */

@RestController
@RequestMapping("/api/sys-dict")
@Api(tags = "字典管理")
@Validated
public class SysDictController {

    @Resource
    private ISysDictService sysDictService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:sys-dict:list')")
    @ApiOperation(value = "字典分页查询", notes = "server:sys-dict:list")
    public PageResult<SysDictPageResp> pageList(SysDictPageReq req) {
        return PageResult.ok(sysDictService.pageList(req));
    }

    @PostMapping("/import")
    @PreAuthorize("@ss.hasPermission('server:sys-dict:import')")
    @ApiOperation(value = "字典列表导入", notes = "server:sys-dict:import")
    @OperateLog(title = "字典列表导入", operateType = OperateType.IMPORT)
    public ApiResult<?> importDemoExcel(@ApiParam(value = "file") @RequestPart("file") MultipartFile file) {
        sysDictService.importExcel(file);
        return ApiResult.ok(SysStatus.IMPORT_SUCCESS);
    }

    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermission('server:sys-dict:export')")
    @ApiOperation(value = "字典列表导出", notes = "server:sys-dict:export")
    @OperateLog(title = "字典列表导出", operateType = OperateType.EXPORT)
    public void exportList(HttpServletResponse response, @RequestBody SysDictExportReq req) {
        List<SysDictExportResp> dataList = sysDictService.exportList(req);
        ExcelUtils.write(response, "列表数据", SysDictExportResp.class, dataList);
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('server:sys-dict:add')")
    @ApiOperation(value = "新增字典", notes = "server:sys-dict:add")
    @OperateLog(title = "新增字典", operateType = OperateType.ADD)
    public ApiResult<Long> add(@RequestBody @Valid SysDictAddReq req) {
        long id = sysDictService.add(req);
        return ApiResult.ok(id);
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermission('server:sys-dict:edit')")
    @ApiOperation(value = "修改字典", notes = "server:sys-dict:edit")
    @OperateLog(title = "修改字典", operateType = OperateType.UPDATE)
    public ApiResult<?> edit(@RequestBody @Valid SysDictEditReq req) {
        sysDictService.edit(req);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:sys-dict:info')")
    @ApiOperation(value = "字典查询详情", notes = "server:sys-dict:info")
    @ApiImplicitParam(name = "id", value = "字典id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<SysDictResp> findOne(@RequestParam("id") @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        return ApiResult.ok(sysDictService.findOne(id));
    }

    @DeleteMapping
    @PreAuthorize("@ss.hasPermission('server:sys-dict:delete')")
    @ApiOperation(value = "删除字典", notes = "server:sys-dict:delete")
    @OperateLog(title = "删除字典", operateType = OperateType.DELETE)
    public ApiResult<?> delete(@RequestBody @NotEmpty(message = "字典编码缺失") List<@NotBlank(message = "字典编码缺失") String> codeList) {
        sysDictService.delete(codeList);
        return ApiResult.ok();
    }

    @GetMapping("/refresh-cache")
    @PreAuthorize("@ss.hasPermission('server:sys-dict:refresh-cache')")
    @ApiOperation(value = "刷新字典缓存", notes = "server:sys-dict:refresh-cache")
    public ApiResult<?> refreshCache() {
        sysDictService.refreshCache();
        return ApiResult.ok();
    }

}
