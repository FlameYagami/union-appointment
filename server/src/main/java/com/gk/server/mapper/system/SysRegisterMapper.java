package com.gk.server.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.server.model.dto.system.sysRegister.SysRegisterPageReq;
import com.gk.server.model.dto.system.sysRegister.SysRegisterPageResp;
import com.gk.server.model.entity.system.SysRegister;
import org.apache.ibatis.annotations.Param;

/**
 * 用户注册表 Mapper 接口
 *
 * @author Flame
 * @since 2024-05-13 09:30:02
 */

public interface SysRegisterMapper extends BaseMapper<SysRegister> {

    /**
     * 分页查询
     */
    IPage<SysRegisterPageResp> pageSysRegister(IPage<?> page, @Param("req") SysRegisterPageReq req);

}
