package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.annotation.RepeatSubmit;
import com.gk.server.model.dto.system.sysRegister.*;
import com.gk.server.service.intf.system.ISysRegisterService;
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
 * 用户注册表 控制类
 *
 * @author Flame
 * @since 2024-05-13 09:30:02
 */

@RestController
@RequestMapping("/api/sys-register")
@Api(tags = "用户注册管理")
@Validated
public class SysRegisterController {

    @Resource
    private ISysRegisterService sysRegisterService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:sys-register:page')")
    @ApiOperation(value = "用户注册分页查询", notes = "server:sys-register:page")
    public PageResult<SysRegisterPageResp> pageList(SysRegisterPageReq req) {
        return PageResult.ok(sysRegisterService.pageList(req));
    }

    @PostMapping("/register")
    @RepeatSubmit
    @ApiOperation(value = "新增用户注册")
    public ApiResult<Long> register(@RequestBody @Valid SysRegisterReq req) {
        long id = sysRegisterService.register(req);
        return ApiResult.ok(id);
    }

    @GetMapping("/register-status")
    @ApiOperation(value = "注册状态查询")
    public ApiResult<String> getRegisterStatus(SysRegisterStatusReq req) {
        return ApiResult.ok(sysRegisterService.getRegisterStatus(req));
    }

    @PutMapping("/review")
    @PreAuthorize("@ss.hasPermission('server:sys-register:review')")
    @ApiOperation(value = "审核用户注册", notes = "server:sys-register:review")
    @OperateLog(title = "审核用户注册", operateType = OperateType.UPDATE)
    public ApiResult<?> review(@RequestBody @Valid SysRegisterReviewReq req) {
        sysRegisterService.review(req);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:sys-register:info')")
    @ApiOperation(value = "用户注册查询详情", notes = "server:sys-register:info")
    @ApiImplicitParam(name = "id", value = "用户注册id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<SysRegisterResp> findOne(@RequestParam("id") @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        return ApiResult.ok(sysRegisterService.findOne(id));
    }

}
