package com.gk.framework.model.dto.generate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 前端代码生成 表字段请求类
 *
 * @author GuoYu
 * @since 2024-03-23 10:29
 **/
@ApiModel(value = "数据库表字段请求")
@Data
@Accessors(chain = true)
public class GenColumnReq {
    @ApiModelProperty(value = "字段名", required = true, example = "字段名")
    @NotNull(message = "字段名不能为空")
    private String columnName;

    @ApiModelProperty(value = "字段描述", required = true, example = "字段描述")
    @NotNull(message = "字段描述不能为空")
    private String columnComment;

    @ApiModelProperty(value = "字段类型", required = true, example = "varchar")
    @NotNull(message = "字段类型不能为空")
    private String dataType;

    @ApiModelProperty(value = "字符串字段长度", required = true, example = "32")
    private Integer strLength;

    @ApiModelProperty(value = "前端组件类型", required = true, example = "input")
    private String htmlType;

    @ApiModelProperty(value = "字典编码", required = true, example = "yesOrNo")
    private String dictCode;

    /**
     * java属性
     */
    @ApiModelProperty(hidden = true)
    private String javaField;
}
