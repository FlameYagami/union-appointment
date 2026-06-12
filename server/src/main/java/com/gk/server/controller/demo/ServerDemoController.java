package com.gk.server.controller.demo;

import com.gk.common.enums.OperateType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.common.utils.ExcelUtils;
import com.gk.framework.annotation.OperateLog;
import com.gk.server.model.dto.demo.serverDemo.*;
import com.gk.server.service.intf.demo.IServerDemoService;
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
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 示例表 控制类
 *
 * @author GuoYu
 * @since 2023-04-26 16:06:07
 */

@RestController
@RequestMapping("/api/server-demo")
@Api(tags = "示例管理")
@Validated
public class ServerDemoController {

    @Resource
    private IServerDemoService serverDemoService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:server-demo:list')")
    @ApiOperation(value = "示例分页查询", notes = "server:server-demo:list")
    public PageResult<ServerDemoPageResp> pageList(ServerDemoPageReq req) {
        return PageResult.ok(serverDemoService.pageList(req));
    }

    @PostMapping("/import")
    @PreAuthorize("@ss.hasPermission('server:server-demo:import')")
    @ApiOperation(value = "示例列表导入", notes = "server:server-demo:import")
    @OperateLog(title = "示例列表导入", operateType = OperateType.IMPORT)
    public ApiResult<?> importDemoExcel(@ApiParam(value = "file") @RequestPart("file") MultipartFile file) {
        serverDemoService.importDemoExcel(file);
        return ApiResult.ok(SysStatus.IMPORT_SUCCESS);
    }

    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermission('server:server-demo:export')")
    @ApiOperation(value = "示例列表导出", notes = "server:server-demo:export")
    @OperateLog(title = "示例列表导出", operateType = OperateType.EXPORT)
    public void exportList(HttpServletResponse response, @RequestBody ServerDemoExportReq req) {
        List<ServerDemoExportResp> dataList = serverDemoService.exportList(req);
        ExcelUtils.write(response, "列表数据", ServerDemoExportResp.class, dataList);
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('server:server-demo:add')")
    @ApiOperation(value = "新增示例", notes = "server:server-demo:add")
    @OperateLog(title = "新增示例", operateType = OperateType.ADD)
    public ApiResult<Long> add(@RequestBody @Valid ServerDemoAddReq req) {
        long id = serverDemoService.add(req);
        return ApiResult.ok(id);
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermission('server:server-demo:edit')")
    @ApiOperation(value = "修改示例", notes = "server:server-demo:edit")
    @OperateLog(title = "修改示例", operateType = OperateType.UPDATE)
    public ApiResult<?> edit(@RequestBody @Valid ServerDemoEditReq req) {
        serverDemoService.edit(req);
        return ApiResult.ok();
    }

    @DeleteMapping
    @PreAuthorize("@ss.hasPermission('server:server-demo:delete')")
    @ApiOperation(value = "删除示例", notes = "server:server-demo:edit")
    @OperateLog(title = "删除示例", operateType = OperateType.DELETE)
    public ApiResult<?> delete(@RequestBody @NotEmpty(message = "示例id缺失") List<@Min(value = 1000000000000000001L, message = "id不合法") Long> idList) {
        serverDemoService.delete(idList);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:server-demo:info')")
    @ApiOperation(value = "示例查询详情", notes = "server:server-demo:edit")
    @ApiImplicitParam(name = "id", value = "示例id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<ServerDemoResp> findOne(@RequestParam("id") @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        return ApiResult.ok(serverDemoService.findOne(id));
    }

}
