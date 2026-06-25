package com.gk.wxstaff.controller.system;

import cn.hutool.core.util.StrUtil;
import com.gk.common.enums.SysStatus;
import com.gk.common.enums.YesOrNo;
import com.gk.common.model.exception.SysException;
import com.gk.common.model.others.ApiResult;
import com.gk.common.utils.AesUtils;
import com.gk.common.utils.CarnetUtils;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.enums.SystemConfig;
import com.gk.framework.helper.SysConfigHelper;
import com.gk.framework.model.bo.security.LoginUser;
import com.gk.framework.utils.LoginUserUtils;
import com.gk.security.enums.CaptchaType;
import com.gk.security.model.bo.CaptchaParam;
import com.gk.security.model.dto.login.LoginReq;
import com.gk.security.model.dto.login.PubConfigResp;
import com.gk.security.service.intf.CaptchaService;
import com.gk.security.service.intf.ISysLoginService;
import com.gk.wxstaff.service.intf.system.IWxSysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 工作人员移动端登录管理 控制类
 *
 * @author GuoYu
 * @since 2025-03-03 11:28:23
 */

@RestController
@RequestMapping("/api/wx-staff")
@Api(tags = "Worker端-登录管理")
@Validated
@Slf4j
public class WxWorkerLoginController {

    @Resource
    private CaptchaService blockPuzzleCaptchaServiceImpl;
    @Resource
    private CaptchaService clickWordCaptchaServiceImpl;
    @Resource
    private ISysLoginService sysLoginService;
    @Resource
    private CaptchaParam captchaParam;

    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    @OperateLog(enable = false)
    public ApiResult<?> login(@RequestBody @Valid LoginReq req) {
        String captchaEnable = SysConfigHelper.getInstance().getString(SystemConfig.CAPTCHA_ENABLE);
        // 如果配置了行为验证码关闭, 则不校验。不配置或配置开启都进行校验。
        if (!YesOrNo.NO.value.equals(captchaEnable)) {
            // 行为验证码校验
            CaptchaService captchaService = getCaptchaTypeService(captchaParam.getCaptchaType());
            if (captchaService == null) {
                throw new SysException(SysStatus.CAPTCHA_TYPE_INVALID);
            }
            captchaService.verification(req.getCaptcha());
        }

        // 通关卡校验
        List<String> decryptCarnetList = CarnetUtils.decryptCarnet(req.getCarnet());
        String aesKey = decryptCarnetList.get(0);
        String aesIv = decryptCarnetList.get(1);

        String decryptPassword = AesUtils.decrypt(req.getPassword(), aesKey, aesIv);
        String token = sysLoginService.login(req.getUsername(), decryptPassword, aesKey, aesIv);
        return ApiResult.ok(token);
    }

    @GetMapping("/pub-config")
    @ApiOperation(value = "获取公有配置")
    public ApiResult<PubConfigResp> getPublicConfig() {
        return ApiResult.ok(sysLoginService.getPubConfig());
    }

    /**
     * 获取验证码业务实现类
     *
     * @param captchaType 验证吗类型
     */
    private CaptchaService getCaptchaTypeService(String captchaType) {
        if (StrUtil.isEmpty(captchaType)) {
            return null;
        }
        if (CaptchaType.BLOCK_PUZZLE.getValue().equals(captchaType)) {
            return blockPuzzleCaptchaServiceImpl;
        }
        if (CaptchaType.CLICK_WORD.getValue().equals(captchaType)) {
            return clickWordCaptchaServiceImpl;
        }
        return null;
    }
}
