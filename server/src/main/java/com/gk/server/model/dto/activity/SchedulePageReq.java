package com.gk.server.model.dto.activity;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 排期 分页查询请求
 *
 * @author Codex
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "排期分页查询请求")
public class SchedulePageReq extends PageDateReq {

    @ApiModelProperty(value = "场地ID", example = "1000000000000000001")
    private Long venueId;

    @ApiModelProperty(value = "排期名称", example = "周一")
    private String name;

    @ApiModelProperty(value = "预约日期", example = "2026-06-20")
    private String scheduleDate;

    @ApiModelProperty(value = "状态：0-关闭，1-开启", example = "1")
    private String status;

    /**
     * 处理查询条件
     */
    public void handleData() {
    }
}