package com.gk.framework.service.impl.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.framework.mapper.system.SysBlacklistMapper;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistExportReq;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistExportResp;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistPageReq;
import com.gk.framework.model.dto.system.sysBlacklist.SysBlacklistPageResp;
import com.gk.framework.model.dto.system.sysUser.BlacklistStatusChangeReq;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.service.intf.system.ISysBlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 黑名单 服务实现类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */

@Service
@Slf4j
public class SysBlacklistService extends ServiceImpl<SysBlacklistMapper, SysUser> implements ISysBlacklistService {

    /**
     * 分页查询
     */
    @Override
    public IPage<SysBlacklistPageResp> pageList(SysBlacklistPageReq req) {
        IPage<SysBlacklistPageResp> resp = baseMapper.pageSysBlacklist(req.createPage(), req);
        resp.getRecords().forEach(SysBlacklistPageResp::handleData);
        return resp;
    }

    /**
     * 全表导出
     */
    @Override
    public List<SysBlacklistExportResp> exportList(SysBlacklistExportReq req) {
        return baseMapper.exportSysBlacklist(req).stream()
                .peek(SysBlacklistExportResp::handleData)
                .collect(Collectors.toList());
    }

    /**
     * 修改黑名单状态
     */
    @Override
    public void changeBlacklistStatus(BlacklistStatusChangeReq req) {
        boolean result = lambdaUpdate()
                .set(SysUser::getBlacklistStatus, req.getBlacklistStatus())
                .eq(SysUser::getId, req.getId())
                .update();
        if (!result) {
            log.error("Change BlacklistStatus Error: id({})", req.getId());
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

}

