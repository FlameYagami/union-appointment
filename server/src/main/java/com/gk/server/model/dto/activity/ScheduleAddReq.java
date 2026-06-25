package com.gk.server.model.dto.activity;

import com.gk.server.model.entity.activity.Schedule;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 排期 新增请求类
 *
 * @author Codex
 */
@ApiModel(value = "排期新增请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleAddReq extends ScheduleBaseReq {

    /**
     * 转换成数据库操作类
     */
    @Override
    public Schedule toEntity() {
        return super.toEntity()
                .setBookedCount(0);
    }
}