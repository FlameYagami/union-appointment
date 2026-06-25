package com.gk.server.model.dto.activity;

import com.gk.framework.validate.InEnum;
import com.gk.server.enums.AppointmentApproveResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 预约审批请求
 *
 * @author Codex
 */
@ApiModel(value = "预约审批请求")
@Data
public class AppointmentApproveReq {

    @Min(value = 1, message = "预约ID不合法")
    @ApiModelProperty(value = "预约ID", required = true, example = "1000000000000000001")
    private long id;

    @NotBlank(message = "审批结果不能为空")
    @InEnum(value = AppointmentApproveResult.class, message = "审批结果不合法")
    @ApiModelProperty(value = "审批结果：approved-通过，rejected-拒绝", required = true, example = "approved")
    private String result;

    @ApiModelProperty(value = "审批备注")
    private String approveRemark;
}