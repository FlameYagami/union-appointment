package com.gk.framework.model.dto.generate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数据库表请求类
 *
 * @author GuoYu
 * @since 2024-03-19 10:25
 **/
@ApiModel(value = "数据库表请求")
@Data
@Accessors(chain = true)
public class GenTableReq {

    @ApiModelProperty(value = "表名", example = "表名")
    private String tableName;

    @ApiModelProperty(value = "表描述", example = "表描述")
    private String tableComment;
}
