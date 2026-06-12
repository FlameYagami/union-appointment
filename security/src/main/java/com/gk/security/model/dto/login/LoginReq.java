package com.gk.security.model.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录请求类
 *
 * @author Flame
 * @date 2022-12-22 14:06
 **/

@ApiModel("用户登录请求")
@Data
public class LoginReq {

    @ApiModelProperty(value = "账号", required = true, example = "admin")
    @NotBlank(message = "账号不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true, example = "admin@123456")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "验证码校验", required = true, example = "verifyCaptcha")
    private String captcha;

    /**
     * 前端生成的AES Key拼接上AES IV, 再由RSA的公钥加密后, 生成通关卡
     */
    @ApiModelProperty(value = "加密通关卡", required = true, example = "carnet")
    @NotBlank(message = "通关卡不能为空")
    private String carnet;

}
