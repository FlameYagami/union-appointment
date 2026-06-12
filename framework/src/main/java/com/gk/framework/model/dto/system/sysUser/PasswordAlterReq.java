package com.gk.framework.model.dto.system.sysUser;

import com.gk.common.constant.RegexConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 用户密码变更(重置密码后需要变更)
 *
 * @author Flame
 * @since 2023-02-27 15:11
 **/
@ApiModel(value = "用户密码变更(重置密码后需要变更)请求")
@Data
public class PasswordAlterReq {

    @ApiModelProperty(value = "新密码", required = true, example = "1")
    @NotNull(message = "请输入新密码")
    @Pattern(regexp = RegexConstant.PASSWORD, message = "密码必须是8-16位的大小写字母、数字、特殊字符组合")
    private String newPassword;


}
