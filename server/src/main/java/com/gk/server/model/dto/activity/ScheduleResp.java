package com.gk.server.model.dto.activity;

import com.gk.common.enums.EnabledType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 排期 响应类
 *
 * @author Codex
 */
@ApiModel(value = "排期响应")
@Data
@Accessors(chain = true)
public class ScheduleResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "场地ID", example = "1000000000000000001")
    private long venueId;

    @ApiModelProperty(value = "场地名称", example = "A栋会议室")
    private String venueName;

    @ApiModelProperty(value = "排期名称", example = "周一上午场")
    private String name;

    @ApiModelProperty(value = "预约日期", example = "2026-06-20")
    private String scheduleDate;

    @ApiModelProperty(value = "开始时间", example = "09:00")
    private String startTime;

    @ApiModelProperty(value = "结束时间", example = "12:00")
    private String endTime;

    @ApiModelProperty(value = "最大预约人数", example = "30")
    private int maxCount;

    @ApiModelProperty(value = "已预约人数", example = "10")
    private int bookedCount;

    @ApiModelProperty(value = "剩余名额", example = "20")
    private int remainCount;

    @ApiModelProperty(value = "状态：0-关闭，1-开放", example = "1")
    private String status;

    @ApiModelProperty(value = "状态名称", example = "开放")
    private String statusName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "可用状态：0-禁用，1-启用", example = "1")
    private String enabled;

    @ApiModelProperty(value = "创建时间", example = "2026-01-01 10:00:00")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", example = "2026-01-01 10:00:00")
    private Date updateTime;

    /**
     * 处理数据
     */
    public void handleData() {
        this.remainCount = maxCount - bookedCount;
        this.statusName = EnabledType.ENABLE.value.equals(status) ? "开放" : "关闭";
    }
}
