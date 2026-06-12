package com.gk.server.model.dto.system.sysUser;

import com.gk.common.constant.CommonConstant;
import com.gk.common.model.dto.PageDateReq;
import com.gk.framework.utils.LoginUserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户 分页请求类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@ApiModel(value = "用户分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserPageReq extends PageDateReq {

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "姓名", example = "姓名")
    private String nickname;

    @ApiModelProperty(value = "手机号", example = "15800000000")
    private String telephone;

    @ApiModelProperty(value = "用户类型(1:管理员)", example = "1")
    private String userType;

    @ApiModelProperty(value = "账号状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

    @ApiModelProperty(value = "超管id(账号不是超管时要排除超管账号)", hidden = true)
    private Long superAdminId;

    public void handleData() {
        if (!LoginUserUtils.isSuperAdmin()) {
            superAdminId = CommonConstant.TOP_ID;
        }
    }


}
