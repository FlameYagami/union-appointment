package com.gk.server.service.intf.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.server.model.dto.activity.*;

/**
 * 预约服务接口
 *
 * @author Codex
 */
public interface IAppointmentService {

    /**
     * 分页查询
     */
    IPage<AppointmentResp> pageList(AppointmentPageReq req);

    /**
     * 新增
     */
    long add(AppointmentAddReq req);

    /**
     * 取消预约
     */
    void cancel(long id);

    /**
     * 审批预约
     */
    void approve(AppointmentApproveReq req);

    /**
     * 单个查询
     */
    AppointmentResp findOne(long id);

    /**
     * 标记违约
     */
    void markViolated(long id);
}
