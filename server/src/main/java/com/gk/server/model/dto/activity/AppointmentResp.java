package com.gk.server.model.dto.activity;

import com.gk.server.enums.AppointmentStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 预约 响应类
 *
 * @author Codex
 */
@ApiModel(value = "预约响应")
@Data
@Accessors(chain = true)
public class AppointmentResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "场地ID", example = "1000000000000000001")
    private long venueId;

    @ApiModelProperty(value = "场地名称", example = "A栋会议室")
    private String venueName;

    @ApiModelProperty(value = "场地类型", example = "venue")
    private String venueType;

    @ApiModelProperty(value = "场地类型名称", example = "场地")
    private String venueTypeName;

    @ApiModelProperty(value = "排期ID", example = "1000000000000000001")
    private long scheduleId;

    @ApiModelProperty(value = "排期名称", example = "周一上午场")
    private String scheduleName;

    @ApiModelProperty(value = "预约人用户ID", example = "1000000000000000001")
    private long userId;

    @ApiModelProperty(value = "预约人账号", example = "zhangsan")
    private String userName;

    @ApiModelProperty(value = "部门ID", example = "1000000000000000001")
    private long deptId;

    @ApiModelProperty(value = "部门名称", example = "综合部")
    private String deptName;

    @ApiModelProperty(value = "预约人数", example = "5")
    private int personCount;

    @ApiModelProperty(value = "预约状态", example = "pending")
    private String status;

    @ApiModelProperty(value = "预约状态名称", example = "待审批")
    private String statusName;

    @ApiModelProperty(value = "预约日期", example = "2026-06-20")
    private String appointmentDate;

    @ApiModelProperty(value = "预约时间段", example = "09:00-12:00")
    private String timeSlot;

    @ApiModelProperty(value = "联系电话", example = "13800138000")
    private String phone;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "审批人", example = "1000000000000000001")
    private Long approveBy;

    @ApiModelProperty(value = "审批人姓名", example = "李四")
    private String approveByName;

    @ApiModelProperty(value = "审批时间", example = "2026-01-01 10:00:00")
    private Date approveTime;

    @ApiModelProperty(value = "审批备注")
    private String approveRemark;

    @ApiModelProperty(value = "可用状态", example = "1")
    private String enabled;

    @ApiModelProperty(value = "创建时间", example = "2026-01-01 10:00:00")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", example = "2026-01-01 10:00:00")
    private Date updateTime;

    public void handleData() {
        AppointmentStatus statusEnum = AppointmentStatus.getInstance(status);
        this.statusName = statusEnum != null ? statusEnum.desc : status;
    }
}
