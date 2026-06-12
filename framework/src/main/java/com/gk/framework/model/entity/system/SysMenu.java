package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.model.base.BaseEntity;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.framework.model.dto.system.sysMenu.SysMenuResp;
import com.gk.framework.utils.DescUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 菜单权限表 数据库模型
 *
 * @author Flame
 * @since 2022-12-29 09:30:38
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 权限标识
     */
    private String permissionCode;

    /**
     * 菜单类型
     */
    private String menuType;

    /**
     * 菜单地址
     */
    private String menuPath;

    /**
     * 菜单组件
     */
    private String menuComponent;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 菜单状态
     */
    private String dataStatus;

    /**
     * 菜单缓存
     */
    private String menuCached;

    /**
     * 菜单显示
     */
    private String menuShow;

    /**
     * 菜单开放
     */
    private String menuOpen;

    /**
     * 是否为内部链接
     */
    private String innerLink;

    /**
     * 激活名称
     */
    private String activeName;

    /**
     * 菜单排序
     */
    private Integer menuOrder;


    public SysMenuResp toResp() {
        return new SysMenuResp()
                .setId(id)
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

    public TreeLabelResp toTreeLabelResp() {
        return new TreeLabelResp(id, parentId, dataStatus)
                .setName(menuName + DescUtils.getDataStatusDesc(dataStatus));
    }

}
