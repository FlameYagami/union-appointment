package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.framework.model.dto.system.sysOperateLog.SysOperateLogPageReq;
import com.gk.framework.model.dto.system.sysOperateLog.SysOperateLogPageResp;
import com.gk.framework.model.entity.system.SysOperateLog;
import org.apache.ibatis.annotations.Param;

/**
 * 操作日志表 Mapper 接口
 *
 * @author Flame
 * @since 2022-12-19 14:43:46
 */

public interface SysOperateLogMapper extends BaseMapper<SysOperateLog> {

    /**
     * 分页查询
     */
    IPage<SysOperateLogPageResp> pageSysOperateLog(IPage<?> page, @Param("req") SysOperateLogPageReq req);

}
