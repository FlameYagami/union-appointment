package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk.framework.model.dto.system.sysConfig.SysConfigExportReq;
import com.gk.framework.model.dto.system.sysConfig.SysConfigExportResp;
import com.gk.framework.model.dto.system.sysConfig.SysConfigPageReq;
import com.gk.framework.model.dto.system.sysConfig.SysConfigPageResp;
import com.gk.framework.model.entity.system.SysConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 配置表 Mapper 接口
 *
 * @author GuoYu
 * @since 2023-03-06 14:56:24
 */

public interface SysConfigMapper extends BaseMapper<SysConfig> {

    /**
     * 分页查询
     */
    IPage<SysConfigPageResp> pageSysConfig(IPage<?> page, @Param("req") SysConfigPageReq req);

    /**
     * 全表导出
     */
    List<SysConfigExportResp> exportSysConfig(@Param("req") SysConfigExportReq req);
}
