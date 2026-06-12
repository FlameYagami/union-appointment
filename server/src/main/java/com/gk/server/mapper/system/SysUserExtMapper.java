package com.gk.server.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.server.model.dto.system.sysUser.SysUserExportReq;
import com.gk.server.model.dto.system.sysUser.SysUserExportResp;
import com.gk.server.model.dto.system.sysUser.SysUserPageReq;
import com.gk.server.model.dto.system.sysUser.SysUserPageResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 Mapper 接口
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */

public interface SysUserExtMapper extends BaseMapper<SysUser> {

    /**
     * 分页查询
     */
    IPage<SysUserPageResp> pageSysUser(IPage<?> page, @Param("req") SysUserPageReq req);

    /**
     * 全表导出
     */
    List<SysUserExportResp> exportSysUser(@Param("req") SysUserExportReq req);

}
