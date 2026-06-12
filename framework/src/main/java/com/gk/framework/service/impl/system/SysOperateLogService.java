package com.gk.framework.service.impl.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.EnabledType;
import com.gk.framework.annotation.DataScope;
import com.gk.framework.mapper.system.SysOperateLogMapper;
import com.gk.framework.model.dto.system.sysOperateLog.SysOperateLogPageReq;
import com.gk.framework.model.dto.system.sysOperateLog.SysOperateLogPageResp;
import com.gk.framework.model.dto.system.sysOperateLog.SysOperateLogResp;
import com.gk.framework.model.entity.system.SysOperateLog;
import com.gk.framework.service.intf.system.ISysOperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 操作日志表 服务实现类
 *
 * @author Flame
 * @since 2022-12-19 14:43:46
 */

@Service
@Slf4j
public class SysOperateLogService extends ServiceImpl<SysOperateLogMapper, SysOperateLog> implements ISysOperateLogService {

    /**
     * 分页查询
     */
    @DataScope(bizTableAlias = "sol")
    @Override
    public IPage<SysOperateLogPageResp> pageList(SysOperateLogPageReq req) {
        IPage<SysOperateLogPageResp> page = baseMapper.pageSysOperateLog(req.createPage(), req);
        page.getRecords().forEach(SysOperateLogPageResp::handleData);
        return page;
    }

    /**
     * 单个查询
     */
    @Override
    public SysOperateLogResp findOne(long id) {
        SysOperateLog sysOperateLog = lambdaQuery()
                .eq(SysOperateLog::getEnabled, EnabledType.ENABLE.value)
                .eq(SysOperateLog::getId, id)
                .one();
        if (sysOperateLog == null) {
            return null;
        }
        return sysOperateLog.toResp();
    }

}

