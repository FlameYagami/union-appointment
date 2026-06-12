package com.gk.framework.model.dto.system.sysMenu;

import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.DataStatus;
import com.gk.common.enums.MenuType;
import com.gk.common.enums.YesOrNo;
import com.gk.framework.model.entity.system.SysMenu;
import com.gk.framework.validate.InEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 菜单权限 基础请求类
 *
 * @author Flame
 * @since 2022-12-29 09:30:38
 */
@Data
public class SysMenuBaseReq {

    @ApiModelProperty(value = "菜单名称[32]", required = true, example = "用户管理")
    @NotBlank(message = "菜单名称不能为空")
    @Length(max = 32, message = "权限名称最多输入32个字符")
    private String menuName;

    @ApiModelProperty(value = "权限标识[64]", example = "system:user:list")
    @Length(max = 64, message = "权限标识最多输入64个字符")
    private String permissionCode;

    @ApiModelProperty(value = "菜单地址[64]", example = "user")
    @Length(max = 64, message = "菜单地址最多输入64个字符")
    private String menuPath;

    @ApiModelProperty(value = "菜单组件[500]", example = "system/user/index")
    @Length(max = 500, message = "菜单组件最多输入500个字符")
    private String menuComponent;

    @ApiModelProperty(value = "菜单图标[32]", example = "user")
    @Length(max = 32, message = "菜单图标最多输入32个字符")
    private String menuIcon;

    @ApiModelProperty(value = "菜单状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

    @ApiModelProperty(value = "菜单缓存(1:是, 0:否)", example = "0")
    @InEnum(YesOrNo.class)
    private String menuCached;

    @ApiModelProperty(value = "菜单显示(1:是, 0:否)", example = "1")
    @InEnum(YesOrNo.class)
    private String menuShow;

    @ApiModelProperty(value = "菜单开放(1:是, 0:否)", example = "1")
    @InEnum(YesOrNo.class)
    private String menuOpen;

    @ApiModelProperty(value = "是否为内部链接(1:是, 0:否)", example = "1")
    @InEnum(YesOrNo.class)
    private String innerLink;

    @ApiModelProperty(value = "激活名称[64]", example = "system")
    @Length(max = 64, message = "激活名称最多输入64个字符")
    private String activeName;

    @ApiModelProperty(value = "菜单排序", required = true, example = "100")
    @Min(value = 1, message = "菜单排序不合法")
    private int menuOrder;

    /**
    * 转换成数据库操作类
    */
    protected SysMenu toEntity(long parentId, String menuType, String parentMenuOpen) {
        // 父级菜单如果只开放给超管, 那么当前菜单强制开放给超管
        if (YesOrNo.NO.value.equals(parentMenuOpen)) {
            this.menuOpen = YesOrNo.NO.value;
        }
        // 目录类型的菜单进行数据处理
        if (MenuType.CATALOG.value.equals(menuType)) {
            menuComponent = CommonConstant.EMPTY;
            menuCached = YesOrNo.YES.value;
            activeName = CommonConstant.EMPTY;
        }
        // 按钮类型的菜单进行数据处理
        if (MenuType.BUTTON.value.equals(menuType)) {
            menuPath = CommonConstant.EMPTY;
            menuComponent = CommonConstant.EMPTY;
            menuIcon = CommonConstant.EMPTY;
            dataStatus = DataStatus.NORMAL.value;
            menuCached = YesOrNo.YES.value;
            menuShow = YesOrNo.YES.value;
            innerLink = YesOrNo.YES.value;
            activeName = CommonConstant.EMPTY;
        }
        return new SysMenu()
                .setParentId(parentId)
                .setMenuName(menuName)
                .setPermissionCode(permissionCode)
                .setMenuType(menuType)
                .setMenuPath(menuPath)
                .setMenuComponent(menuComponent)
                .setMenuIcon(menuIcon)
                .setDataStatus(dataStatus)
                .setMenuCached(menuCached)
                .setMenuShow(menuShow)
                .setInnerLink(innerLink)
                .setActiveName(activeName)
                .setMenuOpen(menuOpen)
                .setMenuOrder(menuOrder);
    }

}
