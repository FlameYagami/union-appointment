package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.framework.model.dto.system.sysOperateLog.SysOperateLogPageReq;
import com.gk.framework.model.dto.system.sysOperateLog.SysOperateLogPageResp;
import com.gk.framework.model.dto.system.sysOperateLog.SysOperateLogResp;
import com.gk.framework.model.entity.system.SysOperateLog;

/**
 * 操作日志表 服务接口类
 *
 * @author Flame
 * @since 2022-12-19 14:43:46
 */
public interface ISysOperateLogService extends IService<SysOperateLog> {

    IPage<SysOperateLogPageResp> pageList(SysOperateLogPageReq req);

    SysOperateLogResp findOne(long id);

}
