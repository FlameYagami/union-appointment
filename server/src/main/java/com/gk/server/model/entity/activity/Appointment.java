package com.gk.server.model.entity.activity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gk.common.model.base.BaseEntity;
import com.gk.server.model.dto.activity.AppointmentResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 棰勭害璁板綍瀹炰綋
 *
 * @author Codex
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("act_appointment")
public class Appointment extends BaseEntity {

    @TableId
    private Long id;
    private Long venueId;
    private Long scheduleId;
    private Long userId;
    private String userName;
    private Long deptId;
    private Integer personCount;
    private String status;
    private String appointmentDate;
    private String timeSlot;
    private String phone;
    private String remark;
    private Long approveBy;
    private Date approveTime;
    private String approveRemark;

    public AppointmentResp toResp() {
        return new AppointmentResp()
                .setId(id)
                .setVenueId(venueId)
                .setScheduleId(scheduleId)
                .setUserId(userId)
                .setUserName(userName)
                .setDeptId(deptId)
                .setPersonCount(personCount)
                .setStatus(status)
                .setAppointmentDate(appointmentDate)
                .setTimeSlot(timeSlot)
                .setPhone(phone)
                .setRemark(remark)
                .setApproveBy(approveBy)
                .setApproveTime(approveTime)
                .setApproveRemark(approveRemark);
    }
}
