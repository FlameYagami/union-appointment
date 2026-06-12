package com.gk.server.model.dto.system.sysRegister;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户注册 响应类
 *
 * @author Flame
 * @since 2024-05-13 09:30:02
 */
@ApiModel(value = "用户注册响应")
@Data
@Accessors(chain = true)
public class SysRegisterResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "用户类型(2:示例角色)", example = "2")
    private String userType;

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "姓名", example = "管理员")
    private String nickname;

    @ApiModelProperty(value = "手机号", example = "手机号")
    private String telephone;

    @ApiModelProperty(value = "证件类型(01:身份证, 03:护照, 99:其他)", example = "01")
    private String cardType;

    @ApiModelProperty(value = "证件号", example = "360000000000000000")
    private String cardNumber;

    @ApiModelProperty(value = "审核状态(1:审核中, 2:通过, 3:驳回)", example = "2")
    private String reviewStatus;

    @ApiModelProperty(value = "审核人账号", example = "审核人账号")
    private String reviewUsername;

    @ApiModelProperty(value = "审核人姓名", example = "审核人姓名")
    private String reviewNickname;

    @ApiModelProperty(value = "审核时间", example = "2000-01-31 23:59:59")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date reviewTime;

    @ApiModelProperty(value = "备注", example = "备注")
    private String remark;

}
