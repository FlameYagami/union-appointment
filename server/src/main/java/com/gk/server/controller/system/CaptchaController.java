package com.gk.server.controller.system;

import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.model.others.ApiResult;
import com.gk.security.enums.CaptchaType;
import com.gk.security.model.bo.CaptchaInfo;
import com.gk.security.model.bo.CaptchaParam;
import com.gk.security.model.dto.captcha.CaptchaCheckReq;
import com.gk.security.model.dto.captcha.base.BaseCaptchaGetResp;
import com.gk.security.service.intf.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 行为验证码 控制类
 *
 * @author Kevin
 * @date 2023-08-11 15:06
 */
@RestController
@RequestMapping("/api/captcha")
@Api(tags = "行为验证码")
@Validated
public class CaptchaController {

    @Resource
    private CaptchaService blockPuzzleCaptchaServiceImpl;

    @Resource
    private CaptchaService clickWordCaptchaServiceImpl;

    @Resource
    private CaptchaParam captchaParam;

    @PostMapping("/get")
    @ApiOperation(value = "获取验证码")
    public ApiResult<BaseCaptchaGetResp> get(HttpServletRequest request) {
        CaptchaInfo captchaInfo = new CaptchaInfo();
        captchaInfo.handleData(request);
        CaptchaService captchaService = getCaptchaTypeService(captchaParam.getCaptchaType());
        if (captchaService == null) {
            throw new SysException(SysStatus.CAPTCHA_TYPE_INVALID);
        }
        return ApiResult.ok(captchaService.get(captchaInfo));
    }

    @PostMapping("/check")
    @ApiOperation(value = "验证码校验")
    public ApiResult<?> check(@RequestBody @Valid CaptchaCheckReq checkReq, HttpServletRequest request) {
        checkReq.handleData(request);
        CaptchaService captchaService = getCaptchaTypeService(captchaParam.getCaptchaType());
        if (captchaService == null) {
            throw new SysException(SysStatus.CAPTCHA_TYPE_INVALID);
        }
        captchaService.check(checkReq);
        return ApiResult.ok();
    }

    private CaptchaService getCaptchaTypeService(String captchaType) {
        if (CaptchaType.BLOCK_PUZZLE.value.equals(captchaType)) {
            return blockPuzzleCaptchaServiceImpl;
        }
        if (CaptchaType.CLICK_WORD.value.equals(captchaType)) {
            return clickWordCaptchaServiceImpl;
        }
        return null;
    }

}
