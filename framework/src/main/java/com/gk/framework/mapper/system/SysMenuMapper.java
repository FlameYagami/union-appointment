package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk.framework.model.dto.system.sysMenu.SysMenuTreeReq;
import com.gk.framework.model.dto.system.sysMenu.SysMenuTreeResp;
import com.gk.framework.model.entity.system.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单权限表 Mapper 接口
 *
 * @author Flame
 * @since 2022-12-29 09:30:38
 */

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 树形结构查询
     */
    List<SysMenuTreeResp> listSysMenuTree(@Param("req") SysMenuTreeReq req);

    /**
     * 获取生效中的父级菜单的子菜单ids
     */
    List<Long> getEnableChildMenuIds(@Param("menuId") long menuId);

    /**
     * 获取生效中的父级菜单的子菜单ids
     */
    List<Long> getChildMenuIds(@Param("menuId") long menuId);

    /**
     * 获取菜单的所有下级菜单
     */
    List<SysMenu> listChildMenu(@Param("menuId") long menuId);

}
