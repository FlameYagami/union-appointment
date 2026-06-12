package com.gk.framework.model.dto.system.sysMenu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 菜单权限 树形结构请求类
 *
 * @author Flame
 * @since 2022-12-29 09:30:38
 */
@ApiModel(value = "菜单权限树形结构请求")
@Data
public class SysMenuTreeReq {

    @ApiModelProperty(value = "菜单名称", example = "用户管理")
    private String menuName;

    @ApiModelProperty(value = "菜单状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

}
