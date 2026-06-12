package com.gk.security.model.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 公有配置响应类
 *
 * @author GuoYu
 * @since 2023-03-14 10:01
 **/
@ApiModel("公有配置响应")
@Data
@Accessors(chain = true)
public class PubConfigResp {

    @ApiModelProperty(value = "加密公钥(1:是, 0:否)", example = "xxxxxxxxxxx")
    private String cryptoKey;

    @ApiModelProperty(value = "加密是否开启(1:是, 0:否)", example = "1")
    private String cryptoEnabled;

    @ApiModelProperty(value = "行为验证码是否开启(1:是, 0:否)", example = "1")
    private String captchaEnable;

    @ApiModelProperty(value = "注册是否开启(1:是, 0:否)", example = "1")
    private String registerEnable;

    @ApiModelProperty(value = "WEB端水印是否开启(1:是, 0:否)", example = "1")
    private String webWatermark;

}
