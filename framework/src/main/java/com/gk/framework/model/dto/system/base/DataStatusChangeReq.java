package com.gk.framework.model.dto.system.base;

import com.gk.common.enums.DataStatus;
import com.gk.framework.validate.InEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 数据状态 修改请求类
 *
 * @author Flame
 * @since 2024-03-19 16:33
 **/
@ApiModel(value = "数据状态修改")
@Data
public class DataStatusChangeReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long id;

    @ApiModelProperty(value = "数据状态(1:正常, 0:停用)", required = true, example = "1")
    @NotNull(message = "数据状态缺失")
    @InEnum(DataStatus.class)
    private String dataStatus;

}
