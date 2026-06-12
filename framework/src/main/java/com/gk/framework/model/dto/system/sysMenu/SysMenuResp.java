package com.gk.framework.model.dto.system.sysMenu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 菜单权限 响应类
 *
 * @author Flame
 * @since 2022-12-29 09:30:38
 */
@Data
@Accessors(chain = true)
public class SysMenuResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "父级id", example = "1000000000000000001")
    private long parentId;

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

    @ApiModelProperty(value = "菜单缓存(1:是, 0:否)", example = "0")
    private String menuCached;

    @ApiModelProperty(value = "菜单显示(1:是, 0:否)", example = "1")
    private String menuShow;

    @ApiModelProperty(value = "菜单开放(1:是, 0:否)", example = "1")
    private String menuOpen;

    @ApiModelProperty(value = "是否为内部链接", example = "1")
    private String innerLink;

    @ApiModelProperty(value = "激活名称", example = "system")
    private String activeName;

    @ApiModelProperty(value = "菜单排序", example = "100")
    private int menuOrder;

}
