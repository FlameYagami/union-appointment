package com.gk.server.model.dto.system.sysRegister;

import com.gk.common.enums.ReviewResult;
import com.gk.framework.validate.InEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 注册 审核请求类
 *
 * @author Flame
 * @since 2024-05-13 09:30:02
 */
@ApiModel(value = "注册审核请求")
@Data
public class SysRegisterReviewReq {

    @ApiModelProperty(value = "主键id", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long id;

    @ApiModelProperty(value = "审核结果(1:通过, 0:驳回)", example = "1")
    @NotNull(message = "审核结果不能为空")
    @InEnum(ReviewResult.class)
    private String reviewResult;

    @ApiModelProperty(value = "备注", example = "备注")
    private String remark;


}
