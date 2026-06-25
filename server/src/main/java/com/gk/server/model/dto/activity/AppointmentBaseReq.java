package com.gk.server.model.dto.activity;

import com.gk.server.model.entity.activity.Appointment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 预约 基础请求类
 *
 * @author Codex
 */
@Data
public class AppointmentBaseReq {

    @Min(value = 1, message = "场地ID不合法")
    @ApiModelProperty(value = "场地ID", required = true, example = "1000000000000000001")
    private long venueId;

    @Min(value = 1, message = "排期ID不合法")
    @ApiModelProperty(value = "排期ID", required = true, example = "1000000000000000001")
    private long scheduleId;

    @Min(value = 1, message = "预约人数不能小于1")
    @ApiModelProperty(value = "预约人数", required = true, example = "5")
    private int personCount;

    @NotBlank(message = "联系电话不能为空")
    @Length(max = 20, message = "联系电话长度不能超过20")
    @ApiModelProperty(value = "联系电话", required = true, example = "13800138000")
    private String phone;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 转换成数据库操作类
     */
    protected Appointment toEntity() {
        return new Appointment()
                .setVenueId(venueId)
                .setScheduleId(scheduleId)
                .setPersonCount(personCount)
                .setPhone(phone)
                .setRemark(remark);
    }
}