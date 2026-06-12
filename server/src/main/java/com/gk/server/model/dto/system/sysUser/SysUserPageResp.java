package com.gk.server.model.dto.system.sysUser;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户 分页响应类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@ApiModel(value = "用户分页响应")
@Data
@Accessors(chain = true)
public class SysUserPageResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "姓名", example = "管理员")
    private String nickname;

    @ApiModelProperty(value = "拥有角色", example = "A角, B角")
    private String roleNames;

    @ApiModelProperty(value = "所属部门", example = "研发部, 测试部")
    private String deptNames;

    @ApiModelProperty(value = "用户类型(1:管理员)", example = "1")
    private String userType;

    @ApiModelProperty(value = "手机号(脱敏)", example = "15800000000")
    private String telephone;

    @ApiModelProperty(value = "是否为系统数据(1:是, 0:否, 默认:0)", example = "0")
    private String systemData;

    @ApiModelProperty(value = "账号状态", example = "账号状态")
    private String dataStatus;

    @ApiModelProperty(value = "是否是黑名单", example = "0 否")
    private String blacklistStatus;

    @ApiModelProperty(value = "创建时间", example = "2022-01-01 00:00:00")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;

    public void lateInit() {
        telephone = DesensitizedUtil.mobilePhone(telephone);
    }

}
