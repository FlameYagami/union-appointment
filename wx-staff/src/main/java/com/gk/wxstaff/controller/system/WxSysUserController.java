package com.gk.wxstaff.controller.system;

import com.gk.common.model.others.ApiResult;
import com.gk.server.model.dto.system.sysUserInfo.SimpleSysUserInfoResp;
import com.gk.server.service.intf.system.ISysUserExtService;
import com.gk.server.service.intf.system.ISysUserInfoService;
import com.gk.wxstaff.service.intf.system.IWxSysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 工作人员移动端登录管理 控制类
 *
 * @author GuoYu
 * @since 2025-03-03 11:28:23
 */

@RestController
@RequestMapping("/api/wx-staff/user")
@Api(tags = "Worker端-用户管理")
@Validated
@Slf4j
public class WxSysUserController {

    @Resource
    private ISysUserInfoService syUserInfoService;
    @Resource
    private IWxSysUserService wxSysUserService;
    @Resource
    private ISysUserExtService sysUserExtService;

    @GetMapping("/simple")
    @ApiOperation(value = "简易用户信息详情查询")
    public ApiResult<SimpleSysUserInfoResp> getSimpleUserInfo() {
        return ApiResult.ok(syUserInfoService.getSimpleSysUserInfo());
    }
}
