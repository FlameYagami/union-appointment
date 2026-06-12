package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistExportReq;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistExportResp;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistPageReq;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistPageResp;
import com.gk.framework.model.dto.system.sysUser.BlacklistStatusChangeReq;
import com.gk.framework.model.entity.system.SysUser;

import java.util.List;

/**
 * 黑名单 服务接口类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
public interface ISysBlacklistService extends IService<SysUser> {

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 分页列表
     */
    IPage<SysBlacklistPageResp> pageList(SysBlacklistPageReq req);

    /**
     * 全表导出
     *
     * @param req 查询条件
     * @return 全表数据
     */
    List<SysBlacklistExportResp> exportList(SysBlacklistExportReq req);

    void changeBlacklistStatus(BlacklistStatusChangeReq req);
}
