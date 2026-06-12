package com.gk.security.model.dto.captcha.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取验证码响应基类
 *
 * @author GuoYu
 * @since 2023-08-22 16:56
 **/
@Data
public class BaseCaptchaGetResp {

    @ApiModelProperty(value = "背景图片", example = "base64图片字符串")
    private String backgroundImage;

    @ApiModelProperty(value = "校验标识", example = "xxxxxxxxxxx")
    private String checkId;

    @ApiModelProperty(value = "密钥", example = "xxxxxxxxxxx")
    private String secretKey;
}
