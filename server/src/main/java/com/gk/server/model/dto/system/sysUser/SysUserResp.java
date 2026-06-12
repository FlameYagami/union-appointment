package com.gk.server.model.dto.system.sysUser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 用户 响应类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@Data
@Accessors(chain = true)
public class SysUserResp {

    // ------------------------ 以下属性为[用户]表包含的信息 ------------------------

    @ApiModelProperty(value = "用户id", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "姓名", example = "管理员")
    private String nickname;

    @ApiModelProperty(value = "是否为系统数据(1:是, 0:否, 默认:0)", example = "0")
    private String systemData;

    @ApiModelProperty(value = "账号状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

    // ------------------------ 以下属性为[部门]表, [角色]表包含的信息 ------------------------

    @ApiModelProperty(value = "部门id集合", example = "[1000000000000000001,1000000000000000002]")
    private List<Long> deptIds;

    @ApiModelProperty(value = "角色id集合", example = "[1000000000000000001,1000000000000000002]")
    private List<Long> roleIds;

    // ------------------------ 以下属性为[用户信息]表包含的信息 ------------------------

    @ApiModelProperty(value = "用户类型(1:管理员)", example = "1")
    private String userType;

    @ApiModelProperty(value = "手机号", example = "15800000000")
    private String telephone;

    @ApiModelProperty(value = "证件类型(01:身份证, 03:护照, 99:其他)", example = "01")
    private String cardType;

    @ApiModelProperty(value = "证件号", example = "360000000000000000")
    private String cardNumber;

    @ApiModelProperty(value = "生日", example = "2000-01-31")
    @JsonFormat(pattern = DateConstant.DATE_PATTERN)
    private Date birthday;

}
