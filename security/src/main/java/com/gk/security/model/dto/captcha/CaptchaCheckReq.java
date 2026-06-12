package com.gk.security.model.dto.captcha;

import com.gk.security.model.bo.CaptchaInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 验证码校验请求类
 *
 * @author GuoYu
 * @since 2023-08-22 16:25
 **/
@ApiModel("验证码校验请求")
@Data
public class CaptchaCheckReq extends CaptchaInfo {

    @ApiModelProperty(value = "校验标识", example = "xxxxxxxxxxx")
    @NotBlank(message = "校验标识不能为空")
    private String checkId;

    @ApiModelProperty(value = "坐标信息", example = "xxxxxxxxxxx")
    @NotBlank(message = "坐标信息不能为空")
    private String pointJson;
}
