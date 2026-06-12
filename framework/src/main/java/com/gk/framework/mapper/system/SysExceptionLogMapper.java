package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.framework.model.dto.system.sysExceptionLog.SysExceptionLogExportReq;
import com.gk.framework.model.dto.system.sysExceptionLog.SysExceptionLogExportResp;
import com.gk.framework.model.dto.system.sysExceptionLog.SysExceptionLogPageReq;
import com.gk.framework.model.dto.system.sysExceptionLog.SysExceptionLogPageResp;
import com.gk.framework.model.entity.system.SysExceptionLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常日志表 Mapper 接口
 *
 * @author GuoYu
 * @since 2024-05-15 15:16:18
 */

public interface SysExceptionLogMapper extends BaseMapper<SysExceptionLog> {

    /**
     * 分页查询
     */
    IPage<SysExceptionLogPageResp> pageSysExceptionLog(IPage<?> page, @Param("req") SysExceptionLogPageReq req);

    /**
     * 全表导出
     */
    List<SysExceptionLogExportResp> exportSysExceptionLog(@Param("req") SysExceptionLogExportReq req);
}
