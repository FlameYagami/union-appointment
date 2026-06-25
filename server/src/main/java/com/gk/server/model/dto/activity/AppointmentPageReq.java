package com.gk.server.model.dto.activity;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 预约 分页查询请求
 *
 * @author Codex
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "预约分页查询请求")
public class AppointmentPageReq extends PageDateReq {

    @ApiModelProperty(value = "场地ID", example = "1000000000000000001")
    private Long venueId;

    @ApiModelProperty(value = "排期ID", example = "1000000000000000001")
    private Long scheduleId;

    @ApiModelProperty(value = "预约用户ID", example = "1000000000000000001")
    private Long userId;

    @ApiModelProperty(value = "预约人姓名", example = "张三")
    private String userName;

    @ApiModelProperty(value = "预约状态", example = "pending")
    private String status;

    @ApiModelProperty(value = "预约日期", example = "2026-06-20")
    private String appointmentDate;

    @ApiModelProperty(value = "联系电话", example = "13800138000")
    private String phone;

    /**
     * 处理查询条件
     */
    public void handleData() {
    }
}