package com.gk.framework.model.dto.system.sysUser;

import com.gk.common.enums.DataStatus;
import com.gk.framework.validate.InEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 用户状态修改
 *
 * @author Flame
 * @date 2023-02-24 15:11
 **/
@ApiModel(value = "用户状态修改请求")
@Data
public class UserStatusChangeReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000002L, message = "id不合法") // 超管角色校验
    private long id;

    @ApiModelProperty(value = "用户状态(1:正常, 0:停用)", required = true, example = "1")
    @NotNull(message = "用户状态缺失")
    @InEnum(DataStatus.class)
    private String dataStatus;

}
