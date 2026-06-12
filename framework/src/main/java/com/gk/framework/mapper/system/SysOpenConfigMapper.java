package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigPageReq;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigPageResp;
import com.gk.framework.model.entity.system.SysOpenConfig;
import org.apache.ibatis.annotations.Param;

/**
 * 第三方对接配置表 Mapper 接口
 *
 * @author Flame
 * @since 2023-03-13 16:26:48
 */

public interface SysOpenConfigMapper extends BaseMapper<SysOpenConfig> {

    /**
     * 分页查询
     */
    IPage<SysOpenConfigPageResp> pageSysOpenConfig(IPage<?> page, @Param("req") SysOpenConfigPageReq req);

}
