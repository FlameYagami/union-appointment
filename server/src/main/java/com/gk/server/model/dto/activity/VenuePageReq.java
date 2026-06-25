package com.gk.server.model.dto.activity;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 场地 分页查询请求
 *
 * @author Codex
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "场地分页查询请求")
public class VenuePageReq extends PageDateReq {

    @ApiModelProperty(value = "场地名称", example = "会议室")
    private String name;

    @ApiModelProperty(value = "场地类型：venue-场地，course-课程，activity-活动", example = "venue")
    private String venueType;

    @ApiModelProperty(value = "状态：0-禁用，1-启用", example = "1")
    private String enabled;

    /**
     * 处理查询条件
     */
    public void handleData() {
    }
}