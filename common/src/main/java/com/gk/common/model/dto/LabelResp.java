package com.gk.common.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 标签响应
 *
 * @author Flame
 * @date 2023-02-03 15:18
 **/

@ApiModel("标签响应")
@Data
@Accessors(chain = true)
public class LabelResp {

    @ApiModelProperty(value = "标签id", example = "1")
    private long id;

    @ApiModelProperty(value = "标签名称", example = "1")
    private String name;

    @ApiModelProperty(value = "节点是否禁用", example = "false")
    private boolean disabled;

}
