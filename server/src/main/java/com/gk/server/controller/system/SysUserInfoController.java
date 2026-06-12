package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.model.others.ApiResult;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.model.dto.system.sysFile.SysFileResp;
import com.gk.server.constant.PathConstant;
import com.gk.server.enums.BizType;
import com.gk.server.model.dto.system.sysUserInfo.SimpleSysUserInfoResp;
import com.gk.server.model.dto.system.sysUserInfo.SysUserInfoEditReq;
import com.gk.server.model.dto.system.sysUserInfo.SysUserInfoResp;
import com.gk.server.service.intf.system.ISysUserInfoService;
import io.swagger.annotations.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户信息表 控制类
 *
 * @author Flame
 * @since 2022-12-16 17:46:27
 */

@RestController
@RequestMapping("/api/user-info")
@Api(tags = "用户信息")
@Validated
public class SysUserInfoController {

    @Resource
    private ISysUserInfoService syUserInfoService;

    @GetMapping("/simple")
    @PreAuthorize("@ss.hasPermission('server:user-info:info')")
    @ApiOperation(value = "简易用户信息详情查询", notes = "server:user-info:info")
    public ApiResult<SimpleSysUserInfoResp> getSimpleUserInfo() {
        return ApiResult.ok(syUserInfoService.getSimpleSysUserInfo());
    }

    @GetMapping("/sys")
    @PreAuthorize("@ss.hasPermission('server:user-info:info')")
    @ApiOperation(value = "用户信息详情查询", notes = "server:user-info:info")
    @ApiImplicitParam(name = "userId", value = "用户id", dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<SysUserInfoResp> getSysUserInfo(Long userId) {
        return ApiResult.ok(syUserInfoService.getSysUserInfo(userId));
    }

    @PutMapping("/sys")
    @PreAuthorize("@ss.hasPermission('server:user-info:edit')")
    @ApiOperation(value = "修改系统用户信息", notes = "server:user-info:edit")
    @OperateLog(title = "修改系统用户信息", operateType = OperateType.UPDATE)
    public ApiResult<?> editSysUserInfo(@RequestBody @Valid SysUserInfoEditReq req) {
        syUserInfoService.editSysUserInfo(req);
        return ApiResult.ok();
    }

    @PostMapping("/sys/upload/avatar")
    @PreAuthorize("@ss.hasPermission('server:user-info:upload')")
    @ApiOperation(value = "系统头像上传", notes = "server:user-info:upload")
    public ApiResult<SysFileResp> uploadSysAvatar(@ApiParam(value = "file") @RequestPart("file") MultipartFile file) {
        return ApiResult.ok(syUserInfoService.uploadSysAvatar(file, PathConstant.AVATAR, BizType.AVATAR.value));
    }


}
