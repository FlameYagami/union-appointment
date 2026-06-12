package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk.framework.model.bo.system.sysUser.SysUserExt;
import com.gk.framework.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表 Mapper 接口
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */

public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询用户扩展信息(包含所属部门、拥有角色)
     */
    SysUserExt findSysUserExt(@Param("userId") long userId);

}
