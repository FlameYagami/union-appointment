package com.gk.server.service.intf.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.server.model.dto.activity.*;

/**
 * 排期服务接口
 *
 * @author Codex
 */
public interface IScheduleService {

    /**
     * 分页查询
     */
    IPage<ScheduleResp> pageList(SchedulePageReq req);

    /**
     * 新增
     */
    long add(ScheduleAddReq req);

    /**
     * 修改
     */
    void edit(ScheduleEditReq req);

    /**
     * 删除
     */
    void delete(long id);

    /**
     * 单个查询
     */
    ScheduleResp findOne(long id);
}
