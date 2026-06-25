package com.gk.server.controller.activity;

import com.gk.common.enums.OperateType;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.annotation.RepeatSubmit;
import com.gk.server.model.dto.activity.*;
import com.gk.server.service.intf.activity.IAppointmentService;
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
 * 预约管理 控制类
 *
 * @author Codex
 */
@RestController
@RequestMapping("/api/activity/appointment")
@Api(tags = "预约管理")
@Validated
public class AppointmentController {

    @Resource
    private IAppointmentService appointmentService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:activity-appointment:page')")
    @ApiOperation(value = "预约分页查询", notes = "server:activity-appointment:page")
    public PageResult<AppointmentResp> pageList(AppointmentPageReq req) {
        return PageResult.ok(appointmentService.pageList(req));
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('server:activity-appointment:add')")
    @RepeatSubmit
    @OperateLog(title = "新增预约", operateType = OperateType.ADD)
    @ApiOperation(value = "新增预约", notes = "server:activity-appointment:add")
    public ApiResult<Long> add(@RequestBody @Valid AppointmentAddReq req) {
        return ApiResult.ok(appointmentService.add(req));
    }

    @PostMapping("/cancel")
    @PreAuthorize("@ss.hasPermission('server:activity-appointment:cancel')")
    @RepeatSubmit
    @OperateLog(title = "取消预约", operateType = OperateType.UPDATE)
    @ApiOperation(value = "取消预约", notes = "server:activity-appointment:cancel")
    public ApiResult<?> cancel(@RequestParam @Min(1L) long id) {
        appointmentService.cancel(id);
        return ApiResult.ok();
    }

    @PostMapping("/approve")
    @PreAuthorize("@ss.hasPermission('server:activity-appointment:approve')")
    @RepeatSubmit
    @OperateLog(title = "审批预约", operateType = OperateType.UPDATE)
    @ApiOperation(value = "审批预约", notes = "server:activity-appointment:approve")
    public ApiResult<?> approve(@RequestBody @Valid AppointmentApproveReq req) {
        appointmentService.approve(req);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:activity-appointment:info')")
    @ApiOperation(value = "预约详情", notes = "server:activity-appointment:info")
    @ApiImplicitParam(name = "id", value = "预约id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<AppointmentResp> findOne(@RequestParam @Min(1L) long id) {
        return ApiResult.ok(appointmentService.findOne(id));
    }

    @PostMapping("/violate")
    @PreAuthorize("@ss.hasPermission('server:activity-appointment:violate')")
    @RepeatSubmit
    @OperateLog(title = "标记违约", operateType = OperateType.UPDATE)
    @ApiOperation(value = "标记违约", notes = "server:activity-appointment:violate")
    public ApiResult<?> markViolated(@RequestParam @Min(1L) long id) {
        appointmentService.markViolated(id);
        return ApiResult.ok();
    }
}
