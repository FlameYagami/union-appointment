package com.gk.server.model.dto.activity;

import com.gk.server.enums.AppointmentStatus;
import com.gk.server.model.entity.activity.Appointment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 预约 新增请求类
 *
 * @author Codex
 */
@ApiModel(value = "预约新增请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentAddReq extends AppointmentBaseReq {

    @ApiModelProperty(value = "预约状态", example = "approved")
    private String status = AppointmentStatus.APPROVED.value;

    @ApiModelProperty(value = "预约日期", example = "2026-06-20")
    private String appointmentDate;

    @ApiModelProperty(value = "预约时间段", example = "09:00-12:00")
    private String timeSlot;

    /**
     * 转换成数据库操作类
     */
    public Appointment toEntity(long userId, String userName, long deptId) {
        return super.toEntity()
                .setUserId(userId)
                .setUserName(userName)
                .setDeptId(deptId)
                .setStatus(status)
                .setAppointmentDate(appointmentDate)
                .setTimeSlot(timeSlot);
    }
}