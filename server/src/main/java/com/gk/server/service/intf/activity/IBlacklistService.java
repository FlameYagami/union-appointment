package com.gk.server.service.intf.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.server.model.dto.activity.*;

/**
 * 黑名单服务接口
 *
 * @author Codex
 */
public interface IBlacklistService {

    /**
     * 分页查询
     */
    IPage<BlacklistResp> pageList(BlacklistPageReq req);

    /**
     * 新增
     */
    long add(BlacklistAddReq req);

    /**
     * 修改
     */
    void edit(BlacklistEditReq req);

    /**
     * 解除封禁
     */
    void unblock(long id);

    /**
     * 单个查询
     */
    BlacklistResp findOne(long id);
}
