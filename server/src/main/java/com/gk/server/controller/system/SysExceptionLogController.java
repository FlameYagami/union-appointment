package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.model.dto.system.sysExceptionLog.SysExceptionLogPageReq;
import com.gk.framework.model.dto.system.sysExceptionLog.SysExceptionLogPageResp;
import com.gk.framework.model.dto.system.sysUser.BlacklistStatusChangeReq;
import com.gk.framework.service.intf.system.ISysBlacklistService;
import com.gk.framework.service.intf.system.ISysExceptionLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 异常日志表 控制类
 *
 * @author Flame
 * @since 2022-12-19 14:43:46
 */

@RestController
@RequestMapping("/api/sys-exception-log")
@Api(tags = "异常日志")
@Validated
public class SysExceptionLogController {

    @Resource
    private ISysExceptionLogService sysExceptionLogService;

    @Resource
    private ISysBlacklistService sysBlacklistService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:sys-exception-log:page')")
    @ApiOperation(value = "异常日志分页查询", notes = "server:sys-exception-log:page")
    public PageResult<SysExceptionLogPageResp> pageList(SysExceptionLogPageReq req) {
        return PageResult.ok(sysExceptionLogService.pageList(req));
    }

    @PostMapping("/blacklist/change")
    @PreAuthorize("@ss.hasPermission('server:sys-exception-log:edit')")
    @ApiOperation(value = "修改黑名单状态", notes = "server:sys-exception-log:edit")
    @OperateLog(title = "修改黑名单状态", operateType = OperateType.UPDATE)
    public ApiResult<?> changeBlacklistStatus(@RequestBody @Valid BlacklistStatusChangeReq req) {
        sysBlacklistService.changeBlacklistStatus(req);
        return ApiResult.ok();
    }


}
