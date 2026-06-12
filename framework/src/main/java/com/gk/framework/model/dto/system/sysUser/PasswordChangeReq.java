package com.gk.framework.model.dto.system.sysUser;

import com.gk.common.constant.RegexConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 用户密码修改
 *
 * @author Flame
 * @since 2023-02-27 15:11
 **/
@ApiModel(value = "用户密码修改请求")
@Data
public class PasswordChangeReq {

    @ApiModelProperty(value = "用户id", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long id;

    @ApiModelProperty(value = "原密码", required = true, example = "1")
    @NotNull(message = "请输入原密码")
    @Pattern(regexp = RegexConstant.PASSWORD, message = "密码必须是8-16位的大小写字母、数字、特殊字符组合")
    private String oldPassword;

    @ApiModelProperty(value = "新密码", required = true, example = "1")
    @Pattern(regexp = RegexConstant.PASSWORD, message = "密码必须是8-16位的大小写字母、数字、特殊字符组合")
    @NotNull(message = "请输入新密码")
    private String newPassword;

}
