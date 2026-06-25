package com.gk.server.controller.activity;

import com.gk.common.enums.OperateType;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.annotation.RepeatSubmit;
import com.gk.server.model.dto.activity.*;
import com.gk.server.service.intf.activity.IBlacklistService;
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
 * 黑名单管理 控制类
 *
 * @author Codex
 */
@RestController
@RequestMapping("/api/activity/blacklist")
@Api(tags = "黑名单管理")
@Validated
public class BlacklistController {

    @Resource
    private IBlacklistService blacklistService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:activity-blacklist:page')")
    @ApiOperation(value = "黑名单分页查询", notes = "server:activity-blacklist:page")
    public PageResult<BlacklistResp> pageList(BlacklistPageReq req) {
        return PageResult.ok(blacklistService.pageList(req));
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('server:activity-blacklist:add')")
    @RepeatSubmit
    @OperateLog(title = "加入黑名单", operateType = OperateType.ADD)
    @ApiOperation(value = "加入黑名单", notes = "server:activity-blacklist:add")
    public ApiResult<Long> add(@RequestBody @Valid BlacklistAddReq req) {
        return ApiResult.ok(blacklistService.add(req));
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermission('server:activity-blacklist:edit')")
    @OperateLog(title = "修改黑名单", operateType = OperateType.UPDATE)
    @ApiOperation(value = "修改黑名单", notes = "server:activity-blacklist:edit")
    public ApiResult<?> edit(@RequestBody @Valid BlacklistEditReq req) {
        blacklistService.edit(req);
        return ApiResult.ok();
    }

    @PostMapping("/unblock")
    @PreAuthorize("@ss.hasPermission('server:activity-blacklist:unblock')")
    @RepeatSubmit
    @OperateLog(title = "解除黑名单", operateType = OperateType.UPDATE)
    @ApiOperation(value = "解除黑名单", notes = "server:activity-blacklist:unblock")
    public ApiResult<?> unblock(@RequestParam @Min(1L) long id) {
        blacklistService.unblock(id);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:activity-blacklist:info')")
    @ApiOperation(value = "黑名单详情", notes = "server:activity-blacklist:info")
    @ApiImplicitParam(name = "id", value = "黑名单id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<BlacklistResp> findOne(@RequestParam @Min(1L) long id) {
        return ApiResult.ok(blacklistService.findOne(id));
    }
}
