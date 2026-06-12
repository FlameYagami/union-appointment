package com.gk.framework.model.dto.generate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 前端代码生成 表字段响应类
 *
 * @author GuoYu
 * @since 2024-03-15 16:21
 **/

@ApiModel(value = "表字段响应")
@Data
@Accessors(chain = true)
public class GenColumnResp {

    @ApiModelProperty(value = "字段名", example = "字段名")
    private String columnName;

    @ApiModelProperty(value = "字段描述", example = "字段描述")
    private String columnComment;

    @ApiModelProperty(value = "字段类型", example = "varchar")
    private String dataType;

    @ApiModelProperty(value = "字符串字段长度", example = "32")
    private Integer strLength;

    @ApiModelProperty(value = "前端组件类型", example = "input")
    private String htmlType;

    @ApiModelProperty(value = "字典编码", example = "yesOrNo")
    private String dictCode;
}
