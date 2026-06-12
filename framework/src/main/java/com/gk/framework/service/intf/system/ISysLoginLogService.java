package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.common.enums.OperateStatus;
import com.gk.framework.annotation.DataScope;
import com.gk.framework.model.dto.system.sysLoginLog.SysLoginLogPageReq;
import com.gk.framework.model.dto.system.sysLoginLog.SysLoginLogPageResp;
import com.gk.framework.model.entity.system.SysLoginLog;

/**
 * 系统访问日志情况信息 服务层
 */
public interface ISysLoginLogService extends IService<SysLoginLog> {

    IPage<SysLoginLogPageResp> pageList(SysLoginLogPageReq req);

    void saveSysLoginInfo(String username, Long deptId, String resultMsg, OperateStatus operateStatus);

}
