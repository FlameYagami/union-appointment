package com.gk.server.controller.activity;

import com.gk.common.enums.OperateType;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.annotation.RepeatSubmit;
import com.gk.server.model.dto.activity.*;
import com.gk.server.service.intf.activity.IScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * 排期管理 控制类
 *
 * @author Codex
 */
@RestController
@RequestMapping("/api/activity/schedule")
@Api(tags = "排期管理")
@Validated
public class ScheduleController {

    @Resource
    private IScheduleService scheduleService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:activity-schedule:page')")
    @ApiOperation(value = "排期分页查询", notes = "server:activity-schedule:page")
    public PageResult<ScheduleResp> pageList(SchedulePageReq req) {
        return PageResult.ok(scheduleService.pageList(req));
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('server:activity-schedule:add')")
    @RepeatSubmit
    @OperateLog(title = "新增排期", operateType = OperateType.ADD)
    @ApiOperation(value = "新增排期", notes = "server:activity-schedule:add")
    public ApiResult<Long> add(@RequestBody @Valid ScheduleAddReq req) {
        return ApiResult.ok(scheduleService.add(req));
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermission('server:activity-schedule:edit')")
    @OperateLog(title = "修改排期", operateType = OperateType.UPDATE)
    @ApiOperation(value = "修改排期", notes = "server:activity-schedule:edit")
    public ApiResult<?> edit(@RequestBody @Valid ScheduleEditReq req) {
        scheduleService.edit(req);
        return ApiResult.ok();
    }

    @DeleteMapping
    @PreAuthorize("@ss.hasPermission('server:activity-schedule:del')")
    @OperateLog(title = "删除排期", operateType = OperateType.DELETE)
    @ApiOperation(value = "删除排期", notes = "server:activity-schedule:del")
    public ApiResult<?> delete(@RequestParam @Min(1L) long id) {
        scheduleService.delete(id);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:activity-schedule:info')")
    @ApiOperation(value = "排期详情", notes = "server:activity-schedule:info")
    @ApiImplicitParam(name = "id", value = "排期id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<ScheduleResp> findOne(@RequestParam @Min(1L) long id) {
        return ApiResult.ok(scheduleService.findOne(id));
    }
}
