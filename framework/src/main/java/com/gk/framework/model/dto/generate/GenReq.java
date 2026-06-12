package com.gk.framework.model.dto.generate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 前端代码生成 生成请求类
 *
 * @author GuoYu
 * @since 2024-03-23 11:12
 **/
@ApiModel(value = "生成请求")
@Data
@Accessors(chain = true)
public class GenReq {
    @ApiModelProperty(value = "表名", required = true, example = "表名")
    private String tableName;

    @ApiModelProperty(value = "表描述", required = true, example = "表描述")
    private String tableComment;

    @ApiModelProperty(value = "模块名", example = "模块名")
    private String moduleName;

    @NotEmpty(message = "表字段不能为空")
    private List<GenColumnReq> columnList;
}
