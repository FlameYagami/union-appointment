package com.gk.server.service.intf.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.server.model.dto.activity.*;

/**
 * 场地服务接口
 *
 * @author Codex
 */
public interface IVenueService {

    /**
     * 分页查询
     */
    IPage<VenueResp> pageList(VenuePageReq req);

    /**
     * 新增
     */
    long add(VenueAddReq req);

    /**
     * 修改
     */
    void edit(VenueEditReq req);

    /**
     * 删除
     */
    void delete(long id);

    /**
     * 单个查询
     */
    VenueResp findOne(long id);

    /**
     * 修改状态
     */
    void changeStatus(long id, String enabled);
}
