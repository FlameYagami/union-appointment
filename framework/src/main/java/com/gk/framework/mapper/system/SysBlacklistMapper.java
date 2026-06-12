package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistExportReq;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistExportResp;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistPageReq;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistPageResp;
import com.gk.framework.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 黑名单 Mapper 接口
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */

public interface SysBlacklistMapper extends BaseMapper<SysUser> {

    /**
     * 分页查询
     */
    IPage<SysBlacklistPageResp> pageSysBlacklist(IPage<?> page, @Param("req") SysBlacklistPageReq req);

    /**
     * 全表导出
     */
    List<SysBlacklistExportResp> exportSysBlacklist(@Param("req") SysBlacklistExportReq req);

}
