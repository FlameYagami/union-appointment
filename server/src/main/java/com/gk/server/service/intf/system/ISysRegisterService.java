package com.gk.server.service.intf.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.server.model.dto.system.sysRegister.*;
import com.gk.server.model.entity.system.SysRegister;

/**
 * 用户注册表 服务接口类
 *
 * @author Flame
 * @since 2024-05-13 09:30:02
 */
public interface ISysRegisterService extends IService<SysRegister> {

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 分页列表
     */
    IPage<SysRegisterPageResp> pageList(SysRegisterPageReq req);

    long register(SysRegisterReq req);

    String getRegisterStatus(SysRegisterStatusReq req);

    /**
     * 单个查询
     *
     * @param id 主键id
     * @return 响应类
     */
    SysRegisterResp findOne(long id);

    void review(SysRegisterReviewReq req);

    void checkUsernameUnique(String username);


    void checkCardNumberUnique(String idcard);



}
