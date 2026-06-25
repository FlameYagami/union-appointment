package com.gk.server.model.dto.activity;

import com.gk.server.model.entity.activity.Venue;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 场地 新增请求类
 *
 * @author Codex
 */
@ApiModel(value = "场地新增请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class VenueAddReq extends VenueBaseReq {

    /**
     * 转换成数据库操作类
     */
    @Override
    public Venue toEntity() {
        return super.toEntity();
    }
}