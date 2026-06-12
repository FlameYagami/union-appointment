package com.gk.server.model.dto.system.sysRegister;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户注册 分页响应类
 *
 * @author Flame
 * @since 2024-05-13 09:30:02
 */
@ApiModel(value = "用户注册响应")
@Data
@Accessors(chain = true)
public class SysRegisterPageResp {

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

    @ApiModelProperty(value = "审核状态(2:待审核, 3:通过, 4:驳回)", example = "2")
    private String reviewStatus;

    @ApiModelProperty(value = "审核时间", example = "2023-02-01 10:10:10")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date reviewTime;

    @ApiModelProperty(value = "创建时间", example = "2023-02-01 10:10:10")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;

    public void handleData() {
        this.telephone = DesensitizedUtil.mobilePhone(telephone);
    }

}
