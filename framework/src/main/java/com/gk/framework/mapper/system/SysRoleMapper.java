package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.framework.model.bo.role.RoleMenu;
import com.gk.framework.model.bo.role.RolePermission;
import com.gk.framework.model.dto.system.sysRole.SysRolePageReq;
import com.gk.framework.model.dto.system.sysRole.SysRolePageResp;
import com.gk.framework.model.entity.system.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表 Mapper 接口
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */

public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 分页查询
     */
    IPage<SysRolePageResp> pageSysRole(IPage<?> page, @Param("req") SysRolePageReq req);

    /**
     * 获取所有的角色权限(包含停用)
     */
    List<RolePermission> listRolePermission();

    /**
     * 获取所有的角色菜单(包含停用)
     */
    List<RoleMenu> listRoleMenu();

}
