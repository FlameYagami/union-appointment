package com.gk.server.model.dto.activity;

import com.gk.server.model.entity.activity.Appointment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * 预约 修改请求类
 *
 * @author Codex
 */
@ApiModel(value = "预约修改请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentEditReq extends AppointmentBaseReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1, message = "id不合法")
    private long id;

    @ApiModelProperty(value = "预约日期", example = "2026-06-20")
    private String appointmentDate;

    @ApiModelProperty(value = "预约时间段", example = "09:00-12:00")
    private String timeSlot;

    /**
     * 转换成数据库操作类
     */
    public Appointment toEntity() {
        return super.toEntity()
                .setId(id)
                .setAppointmentDate(appointmentDate)
                .setTimeSlot(timeSlot);
    }
}