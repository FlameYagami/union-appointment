package com.gk.framework.model.dto.system.sysMenu;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import com.gk.common.model.base.BaseTreeResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 菜单权限 树形结构应类
 *
 * @author Flame
 * @since 2022-12-29 09:30:38
 */
@ApiModel(value = "菜单权限树形结构响应")
@Data
@Accessors(chain = true)
public class SysMenuTreeResp extends BaseTreeResp<SysMenuTreeResp> {

    @ApiModelProperty(value = "菜单名称", example = "用户管理")
    private String menuName;

    @ApiModelProperty(value = "权限标识", example = "system:user:list")
    private String permissionCode;

    @ApiModelProperty(value = "菜单类型(1:目录, 2:菜单, 3:按钮)", example = "1")
    private String menuType;

    @ApiModelProperty(value = "菜单地址", example = "user")
    private String menuPath;

    @ApiModelProperty(value = "菜单组件", example = "system/user/index")
    private String menuComponent;

    @ApiModelProperty(value = "菜单图标", example = "user")
    private String menuIcon;

    @ApiModelProperty(value = "菜单状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

    @ApiModelProperty(value = "激活名", example = "1")
    private String activeName;

    @ApiModelProperty(value = "菜单排序", example = "100")
    private int menuOrder;

    @ApiModelProperty(value = "创建时间", example = "2022-01-01 00:00:00")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;

}
