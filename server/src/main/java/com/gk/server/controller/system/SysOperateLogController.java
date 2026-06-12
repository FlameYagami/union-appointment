package com.gk.server.controller.system;

import com.gk.framework.model.dto.system.sysOperateLog.SysOperateLogPageReq;
import com.gk.framework.model.dto.system.sysOperateLog.SysOperateLogPageResp;
import com.gk.framework.model.dto.system.sysOperateLog.SysOperateLogResp;
import com.gk.framework.service.intf.system.ISysOperateLogService;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Min;

/**
 * 操作日志表 控制类
 *
 * @author Flame
 * @since 2022-12-19 14:43:46
 */

@RestController
@RequestMapping("/api/sys-operate-log")
@Api(tags = "操作日志")
@Validated
public class SysOperateLogController {

    @Resource
    private ISysOperateLogService sysOperateLogService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:sys-operate-log:list')")
    @ApiOperation(value = "操作日志分页查询", notes = "server:sys-operate-log:list")
    public PageResult<SysOperateLogPageResp> pageList(SysOperateLogPageReq req) {
        return PageResult.ok(sysOperateLogService.pageList(req));
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:sys-operate-log:info')")
    @ApiOperation(value = "操作日志详情查询", notes = "server:sys-operate-log:info")
    @ApiImplicitParam(name = "id", value = "操作日志id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<SysOperateLogResp> findOne(@RequestParam("id") @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        return ApiResult.ok(sysOperateLogService.findOne(id));
    }


}
