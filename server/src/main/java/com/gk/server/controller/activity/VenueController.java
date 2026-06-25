package com.gk.server.controller.activity;

import com.gk.common.enums.OperateType;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.annotation.RepeatSubmit;
import com.gk.server.model.dto.activity.*;
import com.gk.server.service.intf.activity.IVenueService;
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
 * 场地管理 控制类
 *
 * @author Codex
 */
@RestController
@RequestMapping("/api/activity/venue")
@Api(tags = "场地管理")
@Validated
public class VenueController {

    @Resource
    private IVenueService venueService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:activity-venue:page')")
    @ApiOperation(value = "场地分页查询", notes = "server:activity-venue:page")
    public PageResult<VenueResp> pageList(VenuePageReq req) {
        return PageResult.ok(venueService.pageList(req));
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('server:activity-venue:add')")
    @RepeatSubmit
    @OperateLog(title = "新增场地", operateType = OperateType.ADD)
    @ApiOperation(value = "新增场地", notes = "server:activity-venue:add")
    public ApiResult<Long> add(@RequestBody @Valid VenueAddReq req) {
        return ApiResult.ok(venueService.add(req));
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermission('server:activity-venue:edit')")
    @OperateLog(title = "修改场地", operateType = OperateType.UPDATE)
    @ApiOperation(value = "修改场地", notes = "server:activity-venue:edit")
    public ApiResult<?> edit(@RequestBody @Valid VenueEditReq req) {
        venueService.edit(req);
        return ApiResult.ok();
    }

    @DeleteMapping
    @PreAuthorize("@ss.hasPermission('server:activity-venue:del')")
    @OperateLog(title = "删除场地", operateType = OperateType.DELETE)
    @ApiOperation(value = "删除场地", notes = "server:activity-venue:del")
    public ApiResult<?> delete(@RequestParam @Min(1L) long id) {
        venueService.delete(id);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:activity-venue:info')")
    @ApiOperation(value = "场地详情", notes = "server:activity-venue:info")
    @ApiImplicitParam(name = "id", value = "场地id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<VenueResp> findOne(@RequestParam @Min(1L) long id) {
        return ApiResult.ok(venueService.findOne(id));
    }

    @PutMapping("/status")
    @PreAuthorize("@ss.hasPermission('server:activity-venue:edit')")
    @OperateLog(title = "修改场地状态", operateType = OperateType.UPDATE)
    @ApiOperation(value = "修改场地状态", notes = "server:activity-venue:edit")
    public ApiResult<?> changeStatus(@RequestParam @Min(1L) long id, @RequestParam String enabled) {
        venueService.changeStatus(id, enabled);
        return ApiResult.ok();
    }
}
