package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.framework.model.entity.system.SysRoleMenu;

import java.util.List;

/**
 * 角色菜单关系表 服务接口类
 *
 * @author Flame
 * @since 2022-12-29 09:43:13
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    boolean modify(long roleId, List<SysRoleMenu> sysRoleMenus);

    void deleteByMenuId(long menuId);
}
