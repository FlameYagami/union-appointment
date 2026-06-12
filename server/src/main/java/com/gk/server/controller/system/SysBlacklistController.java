package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.common.utils.ExcelUtils;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistExportReq;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistExportResp;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistPageReq;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistPageResp;
import com.gk.framework.model.dto.system.sysUser.BlacklistStatusChangeReq;
import com.gk.framework.service.intf.system.ISysBlacklistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 黑名单 控制类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */

@RestController
@RequestMapping("/api/sys-blacklist")
@Api(tags = "黑名单管理")
@Validated
public class SysBlacklistController {

    @Resource
    private ISysBlacklistService sysBlacklistService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:sys-blacklist:page')")
    @ApiOperation(value = "黑名单分页查询", notes = "server:sys-blacklist:page")
    public PageResult<SysBlacklistPageResp> pageList(SysBlacklistPageReq req) {
        return PageResult.ok(sysBlacklistService.pageList(req));
    }

    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermission('server:sys-blacklist:export')")
    @ApiOperation(value = "黑名单列表导出", notes = "server:sys-blacklist:export")
    @OperateLog(title = "黑名单列表导出", operateType = OperateType.EXPORT)
    public void exportList(HttpServletResponse response, @RequestBody SysBlacklistExportReq req) {
        List<SysBlacklistExportResp> dataList = sysBlacklistService.exportList(req);
        ExcelUtils.write(response, "列表数据", SysBlacklistExportResp.class, dataList);
    }

    @PostMapping("/blacklist/change")
    @PreAuthorize("@ss.hasPermission('server:sys-blacklist:edit')")
    @ApiOperation(value = "修改黑名单状态", notes = "server:sys-blacklist:edit")
    @OperateLog(title = "修改黑名单状态", operateType = OperateType.UPDATE)
    public ApiResult<?> changeBlacklistStatus(@RequestBody @Valid BlacklistStatusChangeReq req) {
        sysBlacklistService.changeBlacklistStatus(req);
        return ApiResult.ok();
    }

}
