package com.gk.framework.mapper.system;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.framework.model.dto.system.sysLoginLog.SysLoginLogPageReq;
import com.gk.framework.model.dto.system.sysLoginLog.SysLoginLogPageResp;
import com.gk.framework.model.entity.system.SysLoginLog;

/**
 * 系统访问日志情况信息 数据层
 */
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

    /**
     * 分页查询
     */
    IPage<SysLoginLogPageResp> pageSysLoginLog(IPage<?> page, SysLoginLogPageReq req);

}
