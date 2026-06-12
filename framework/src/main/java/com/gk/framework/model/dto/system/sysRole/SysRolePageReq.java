package com.gk.framework.model.dto.system.sysRole;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色 分页请求类
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */
@ApiModel(value = "角色分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRolePageReq extends PageDateReq {

    @ApiModelProperty(value = "角色名称", example = "管理员")
    private String roleName;

    @ApiModelProperty(value = "角色编码", example = "admin")
    private String roleCode;

    @ApiModelProperty(value = "角色状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

}
