package com.gk.security.model.dto.sysPermission;

import com.gk.common.model.dto.LabelResp;
import com.gk.framework.model.dto.system.sysRole.SysRoleLabelResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

/**
 * 登录信息响应
 *
 * @author Flame
 * @date 2023-02-07 14:15
 **/

@ApiModel("登录信息响应")
@Data
@Accessors(chain = true)
public class AuthPermissionResp {

    @ApiModelProperty(value = "权限集合", example = "[\"*:*:*\"]")
    private Set<String> permissions;

    @ApiModelProperty(value = "当前部门", example = "1")
    private long deptId;

    @ApiModelProperty(value = "当前角色", example = "1")
    private long roleId;

    @ApiModelProperty(value = "用户id", example = "1000000000000000001")
    private long userId;

    @ApiModelProperty(value = "当前角色编码", example = "admin")
    private String roleCode;

    @ApiModelProperty(value = "姓名", example = "管理员")
    private String nickname;

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "初始密码是否变更过(账号创建以及密码重置都将此值置为0)", example = "1")
    private String passwordChanged;

    /**
     * 部门标签
     */
    private List<LabelResp> deptLabels;

    /**
     * 角色标签
     */
    private List<SysRoleLabelResp> roleLabels;

}
