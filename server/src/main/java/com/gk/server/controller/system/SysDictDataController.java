package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.utils.ExcelUtils;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.model.dto.system.CachedDictData;
import com.gk.framework.model.dto.system.base.DataStatusChangeReq;
import com.gk.framework.model.dto.system.sysDictData.*;
import com.gk.framework.service.intf.system.IRedisCacheService;
import com.gk.framework.service.intf.system.ISysDictDataService;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 字典数据表 控制类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */

@RestController
@RequestMapping("/api/sys-dict-data")
@Api(tags = "字典项管理")
@Validated
public class SysDictDataController {

    @Resource
    private ISysDictDataService sysDictDataService;

    @Resource
    private IRedisCacheService redisCacheService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:sys-dict-data:list')")
    @ApiOperation(value = "字典项分页查询", notes = "server:sys-dict-data:list")
    public PageResult<SysDictDataPageResp> pageList(SysDictDataPageReq req) {
        return PageResult.ok(sysDictDataService.pageList(req));
    }

    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermission('server:sys-dict-data:export')")
    @ApiOperation(value = "字典项列表导出", notes = "server:sys-dict-data:export")
    @OperateLog(title = "字典项列表导出", operateType = OperateType.EXPORT)
    public void exportList(HttpServletResponse response, @RequestBody SysDictDataExportReq req) {
        List<SysDictDataExportResp> dataList = sysDictDataService.exportList(req);
        ExcelUtils.write(response, "列表数据", SysDictDataExportResp.class, dataList);
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('server:sys-dict-data:add')")
    @ApiOperation(value = "新增字典项", notes = "server:sys-dict-data:add")
    @OperateLog(title = "新增字典项", operateType = OperateType.ADD)
    public ApiResult<Long> add(@RequestBody @Valid SysDictDataAddReq req) {
        long id = sysDictDataService.add(req);
        return ApiResult.ok(id);
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermission('server:sys-dict-data:edit')")
    @ApiOperation(value = "修改字典项", notes = "server:sys-dict-data:edit")
    @OperateLog(title = "修改字典项", operateType = OperateType.UPDATE)
    public ApiResult<?> edit(@RequestBody @Valid SysDictDataEditReq req) {
        sysDictDataService.edit(req);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:sys-dict-data:info')")
    @ApiOperation(value = "字典项查询详情", notes = "server:sys-dict-data:info")
    @ApiImplicitParam(name = "id", value = "字典项id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<SysDictDataResp> findOne(@RequestParam("id") @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        return ApiResult.ok(sysDictDataService.findOne(id));
    }

    @PostMapping("/status/change")
    @PreAuthorize("@ss.hasPermission('server:sys-dict-data:edit')")
    @ApiOperation(value = "修改字典数据状态", notes = "server:sys-dict-data:edit")
    @OperateLog(title = "修改字典数据状态", operateType = OperateType.UPDATE)
    public ApiResult<?> changeDataStatus(@RequestBody @Valid DataStatusChangeReq req) {
        sysDictDataService.changeDataStatus(req);
        return ApiResult.ok();
    }

    @GetMapping("/list-dict")
    @PreAuthorize("@ss.hasPermission('server:sys-dict-data:list-dict')")
    @ApiOperation(value = "根据字典编码查询所有对应的字典项", notes = "server:sys-dict-data:list-dict")
    @ApiImplicitParam(name = "dictCode", value = "字典编码", required = true, dataTypeClass = String.class, example = "dict_code")
    public ApiResult<List<CachedDictData>> listCacheDictData(@RequestParam("dictCode") @NotBlank(message = "字典编码缺失") String dictCode) {
        return ApiResult.ok(redisCacheService.getDictData(dictCode));
    }

    @DeleteMapping
    @PreAuthorize("@ss.hasPermission('server:sys-dict-data:delete')")
    @ApiOperation(value = "删除字典项", notes = "server:sys-dict-data:delete")
    @OperateLog(title = "删除字典项", operateType = OperateType.DELETE)
    public ApiResult<?> delete(@RequestBody @Valid SysDictDataDeleteReq req) {
        sysDictDataService.delete(req.getIdList(), req.getDictCode());
        return ApiResult.ok();
    }

}
