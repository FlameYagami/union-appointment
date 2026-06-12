package com.gk.server.model.dto.system.sysUserInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import com.gk.framework.model.bo.system.sysUser.SysUserExt;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户信息 响应类
 *
 * @author Flame
 * @since 2022-12-16 17:46:27
 */
@Data
@Accessors(chain = true)
public class SysUserInfoResp {

    @ApiModelProperty(value = "用户信息id(userInfoId)", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "用户id", example = "1000000000000000001")
    private long userId;

    @ApiModelProperty(value = "用户类型(1:管理员)", required = true, example = "1")
    private String userType;

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "姓名", example = "管理员")
    private String nickname;

    @ApiModelProperty(value = "性别(1:男, 2:女, 9:未知)", example = "1")
    private String gender;

    @ApiModelProperty(value = "手机号", example = "15800000000")
    private String telephone;

    @ApiModelProperty(value = "头像链接", example = "https://domain.com/files/avatar/20220101/5ea69f99-ae68-49ce-b144-8837d636b733.jpg")
    private String avatarUrl;

    @ApiModelProperty(value = "证件类型", example = "证件类型")
    private String cardType;

    @ApiModelProperty(value = "证件号(敏感信息)", example = "360000000000000000")
    private String cardNumber;

    @ApiModelProperty(value = "生日", example = "2000-01-31")
    @JsonFormat(pattern = DateConstant.DATE_PATTERN)
    private Date birthday;

    @ApiModelProperty(value = "所属部门", example = "研发部, 测试部")
    private String deptNames;

    @ApiModelProperty(value = "拥有角色", example = "A角, B角")
    private String roleNames;

    @ApiModelProperty(value = "账号状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

    @ApiModelProperty(value = "创建时间", example = "2022-01-01 00:00:00")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;

    public SysUserInfoResp(SysUserExt sysUserExt) {
        this.dataStatus = sysUserExt.getDataStatus();
        this.deptNames = sysUserExt.getDeptNames();
        this.roleNames = sysUserExt.getRoleNames();
        this.createTime = sysUserExt.getCreateTime();
    }

}
