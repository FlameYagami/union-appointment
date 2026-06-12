package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.common.enums.ExceptionType;
import com.gk.framework.model.dto.system.sysExceptionLog.SysExceptionLogPageReq;
import com.gk.framework.model.dto.system.sysExceptionLog.SysExceptionLogPageResp;
import com.gk.framework.model.entity.system.SysExceptionLog;

/**
 * 异常日志表 服务接口类
 *
 * @author Flame
 * @since 2024-04-08 10:49:10
 */
public interface ISysExceptionLogService extends IService<SysExceptionLog> {

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 分页列表
     */
    IPage<SysExceptionLogPageResp> pageList(SysExceptionLogPageReq req);


    void saveExceptLog(String username, String ip, ExceptionType exceptionType);

    void saveBruteForceLoginLog(String username, String ip);

    void checkAndSaveRemoteLoginLog(String username, String ip);

}
