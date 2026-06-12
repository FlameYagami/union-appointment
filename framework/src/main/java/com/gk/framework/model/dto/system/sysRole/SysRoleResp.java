package com.gk.framework.model.dto.system.sysRole;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 角色 响应类
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */
@Data
@Accessors(chain = true)
public class SysRoleResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "角色名称", example = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色编码", example = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "数据权限", example = "数据权限")
    private String dataScope;

    @ApiModelProperty(value = "角色级别", example = "100")
    private int roleLevel;

    @ApiModelProperty(value = "角色描述", example = "角色描述")
    private String roleDesc;

    @ApiModelProperty(value = "是否为系统数据(1:是, 0:否, 默认:0)", example = "0")
    private String systemData;

    @ApiModelProperty(value = "角色状态", example = "角色状态")
    private String dataStatus;

    @ApiModelProperty(value = "角色排序", example = "100")
    private int roleOrder;

    @ApiModelProperty(value = "级联勾选(1:是, 0:否)", example = "1")
    private String cascadeChecked;

    @ApiModelProperty(value = "菜单id合集", example = "[1,2]")
    private List<Long> menuIds;

}
