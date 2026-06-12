package com.gk.server.controller.system;

import com.gk.framework.model.dto.system.sysLoginLog.SysLoginLogPageReq;
import com.gk.framework.model.dto.system.sysLoginLog.SysLoginLogPageResp;
import com.gk.framework.service.intf.system.ISysLoginLogService;
import com.gk.common.model.others.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登录日志表 控制类
 *
 * @author Flame
 * @since 2022-12-19 14:43:46
 */

@RestController
@RequestMapping("/api/sys-login-log")
@Api(tags = "登录日志")
@Validated
public class SysLoginLogController {

    @Resource
    private ISysLoginLogService sysLoginLogService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:sys-login-log:list')")
    @ApiOperation(value = "登录日志分页查询", notes = "server:sys-login-log:list")
    public PageResult<SysLoginLogPageResp> pageList(SysLoginLogPageReq req) {
        return PageResult.ok(sysLoginLogService.pageList(req));
    }


}
