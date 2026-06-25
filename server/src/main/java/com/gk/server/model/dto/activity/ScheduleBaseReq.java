package com.gk.server.model.dto.activity;

import com.gk.server.model.entity.activity.Schedule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 排期 基础请求类
 *
 * @author Codex
 */
@Data
public class ScheduleBaseReq {

    @Min(value = 1, message = "场地ID不合法")
    @ApiModelProperty(value = "场地ID", required = true, example = "1000000000000000001")
    private long venueId;

    @NotBlank(message = "排期名称不能为空")
    @Length(max = 100, message = "排期名称长度不能超过100")
    @ApiModelProperty(value = "排期名称", required = true, example = "周一上午场")
    private String name;

    @NotBlank(message = "预约日期不能为空")
    @ApiModelProperty(value = "预约日期", required = true, example = "2026-06-20")
    private String scheduleDate;

    @NotBlank(message = "开始时间不能为空")
    @ApiModelProperty(value = "开始时间", required = true, example = "09:00")
    private String startTime;

    @NotBlank(message = "结束时间不能为空")
    @ApiModelProperty(value = "结束时间", required = true, example = "12:00")
    private String endTime;

    @Min(value = 1, message = "最大预约人数不能小于1")
    @ApiModelProperty(value = "最大预约人数", required = true, example = "30")
    private int maxCount;

    @ApiModelProperty(value = "状态：0-关闭，1-开放", example = "1")
    private String status = "1";

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 转换成数据库操作类
     */
    protected Schedule toEntity() {
        return new Schedule()
                .setVenueId(venueId)
                .setName(name)
                .setScheduleDate(scheduleDate)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setMaxCount(maxCount)
                .setStatus(status)
                .setRemark(remark);
    }
}