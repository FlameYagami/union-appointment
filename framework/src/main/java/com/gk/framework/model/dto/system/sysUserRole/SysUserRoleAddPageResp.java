package com.gk.framework.model.dto.system.sysUserRole;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 角色中用户添加 分页响应类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@ApiModel(value = "角色中用户添加分页响应")
@Data
@Accessors(chain = true)
public class SysUserRoleAddPageResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "姓名", example = "管理员")
    private String nickname;

    @ApiModelProperty(value = "所属部门", example = "研发部, 测试部")
    private String deptNames;

    @ApiModelProperty(value = "手机号(脱敏)", example = "15800000000")
    private String telephone;

    @ApiModelProperty(value = "账号状态", example = "账号状态")
    private String dataStatus;

    @ApiModelProperty(value = "创建时间", example = "2022-01-01 00:00:00")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;

    public void lateInit() {
        telephone = DesensitizedUtil.mobilePhone(telephone);
    }

}
