package com.gk.server.model.dto.system.sysRegister;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 注册状态请求类
 *
 * @author Flame
 * @since 2024-05-13 09:30:02
 */
@ApiModel(value = "注册状态请求")
@Data
public class SysRegisterStatusReq {

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "手机号", example = "15800000000")
    private String telephone;

    @ApiModelProperty(value = "证件号", example = "3600000000000000")
    private String cardNumber;


}
