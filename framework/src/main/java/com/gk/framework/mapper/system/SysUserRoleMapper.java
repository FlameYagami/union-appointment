package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.framework.model.dto.system.sysUserRole.SysUserRoleAddPageReq;
import com.gk.framework.model.dto.system.sysUserRole.SysUserRoleAddPageResp;
import com.gk.framework.model.dto.system.sysUserRole.SysUserRolePageReq;
import com.gk.framework.model.dto.system.sysUserRole.SysUserRolePageResp;
import com.gk.framework.model.entity.system.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关系表 Mapper 接口
 *
 * @author Flame
 * @since 2022-12-29 09:44:08
 */

public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 角色用户分页查询
     */
    IPage<SysUserRolePageResp> pageSysRoleUser(IPage<?> page, @Param("req") SysUserRolePageReq req);

    /**
     * 角色用户添加分页查询
     */
    IPage<SysUserRoleAddPageResp> pageSysRoleUserAdd(IPage<?> page, @Param("req") SysUserRoleAddPageReq req);

    /**
     * 通过用户id查询拥有的启用的角色id集合
     */
    List<Long> getEnableRoleIdByUserId(@Param("userId") long userId);


}
