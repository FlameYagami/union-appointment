package com.gk.server.controller.system;

import cn.hutool.core.util.StrUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.enums.YesOrNo;
import com.gk.common.model.exception.SysException;
import com.gk.common.model.others.ApiResult;
import com.gk.common.utils.AesUtils;
import com.gk.common.utils.RsaUtils;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.enums.SystemConfig;
import com.gk.framework.helper.SysConfigHelper;
import com.gk.security.enums.CaptchaType;
import com.gk.security.model.bo.CaptchaParam;
import com.gk.security.model.dto.login.PubConfigResp;
import com.gk.security.model.dto.login.LoginReq;
import com.gk.security.service.intf.CaptchaService;
import com.gk.security.service.intf.ISysLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 用户登录 控制类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */

@RestController
@RequestMapping("/api/sys-login")
@Api(tags = "登录管理")
@Validated
@Slf4j
public class SysLoginController {

    @Resource
    private CaptchaService blockPuzzleCaptchaServiceImpl;
    @Resource
    private CaptchaService clickWordCaptchaServiceImpl;
    @Resource
    private ISysLoginService sysLoginService;
    @Resource
    private CaptchaParam captchaParam;

    /**
     * 通关解密后, 可以拆分成 AES KEY 和 AES IV 两部分, 所以长度是2
     */
    private static final int CARNET_SIZE = 2;

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
        List<String> decryptCarnetList = decryptCarnet(req.getCarnet());
        String aesKey = decryptCarnetList.get(0);
        String aesIv = decryptCarnetList.get(1);

        String decryptPassword = AesUtils.decrypt(req.getPassword(), aesKey, aesIv);
        String token = sysLoginService.login(req.getUsername(), decryptPassword, aesKey, aesIv);
        return ApiResult.ok(token);
    }

    @GetMapping("/pub-config")
    @ApiOperation(value = "获取公有配置")
    public ApiResult<PubConfigResp> getPublicKey() {
        String publicKey = RsaUtils.getPublicKey();
        String cryptoEnabled = SysConfigHelper.getInstance().getString(SystemConfig.CRYPTO_ENABLE);
        String captchaEnable = SysConfigHelper.getInstance().getString(SystemConfig.CAPTCHA_ENABLE);
        String registerEnable = SysConfigHelper.getInstance().getString(SystemConfig.REGISTER_ENABLE);
        String webWatermark = SysConfigHelper.getInstance().getString(SystemConfig.WEB_WATERMARK);
        cryptoEnabled = StrUtil.isEmpty(cryptoEnabled) ? YesOrNo.NO.value : cryptoEnabled;
        captchaEnable = StrUtil.isEmpty(captchaEnable) ? YesOrNo.YES.value : captchaEnable;
        registerEnable = StrUtil.isEmpty(registerEnable) ? YesOrNo.NO.value : registerEnable;
        webWatermark = StrUtil.isEmpty(webWatermark) ? YesOrNo.NO.value : webWatermark;
        PubConfigResp pubConfigResp = new PubConfigResp()
                .setCryptoKey(publicKey)
                .setCryptoEnabled(cryptoEnabled)
                .setCaptchaEnable(captchaEnable)
                .setRegisterEnable(registerEnable)
                .setWebWatermark(webWatermark);
        return ApiResult.ok(pubConfigResp);
    }

    /**
     * 解密通关卡
     * @return AES Key/IV组成的字符串数组
     */
    private List<String> decryptCarnet(String carnet) {
        if (StrUtil.isEmpty(carnet)) {
            log.error("Login Error: carnet is blank");
            throw new SysException(SysStatus.LOGIN_FAILED);
        }
        String privateKey = RsaUtils.getPrivateKey();
        log.debug("Private Key: {}", privateKey);
        String decryptCarnetStr = RsaUtils.decryptBase64(carnet, privateKey);
        log.debug("Decrypt Carnet: {}", decryptCarnetStr);

        List<String> decryptCarnetList = StrUtil.split(decryptCarnetStr, CommonConstant.PIPE);
        if (decryptCarnetList.size() != CARNET_SIZE) {
            log.error("Login Error: carnet split size is not 2");
            throw new SysException(SysStatus.LOGIN_FAILED);
        }
        return decryptCarnetList;
    }

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
