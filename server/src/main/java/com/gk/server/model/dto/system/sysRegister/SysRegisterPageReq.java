package com.gk.server.model.dto.system.sysRegister;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户注册 分页请求类
 *
 * @author Flame
 * @since 2024-05-13 09:30:02
 */
@ApiModel(value = "用户注册分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRegisterPageReq extends PageDateReq {

    @ApiModelProperty(value = "用户类型(2:示例角色)", example = "2")
    private String userType;

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "姓名", example = "管理员")
    private String nickname;

    @ApiModelProperty(value = "手机号", example = "手机号")
    private String telephone;

    @ApiModelProperty(value = "审核状态(2:待审核, 3:通过, 4:驳回)", example = "2")
    private String reviewStatus;


}
