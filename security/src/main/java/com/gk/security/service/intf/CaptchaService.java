package com.gk.security.service.intf;

import com.gk.security.model.bo.CaptchaInfo;
import com.gk.security.model.dto.captcha.CaptchaCheckReq;
import com.gk.security.model.dto.captcha.base.BaseCaptchaGetResp;

/**
 * 行为验证码 业务接口层
 *
 * @author Kevin
 * @date 2023-08-11 15:06
 */
public interface CaptchaService {

    /**
     * 获取验证码
     */
    BaseCaptchaGetResp get(CaptchaInfo captchaInfo);

    /**
     * 核对验证码(前端)
     */
    void check(CaptchaCheckReq checkReq);

    /**
     * 二次校验验证码(后端)
     */
    void verification(String captchaVerify);

}
