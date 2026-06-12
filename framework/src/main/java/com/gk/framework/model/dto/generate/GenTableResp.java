package com.gk.framework.model.dto.generate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 前端代码生成 数据库表响应类
 *
 * @author GuoYu
 * @since 2024-03-15 16:21
 **/
@ApiModel(value = "数据库表响应")
@Data
@Accessors(chain = true)
public class GenTableResp {

    @ApiModelProperty(value = "表名", example = "表名")
    private String tableName;

    @ApiModelProperty(value = "表描述", example = "表描述")
    private String tableComment;

    @ApiModelProperty(value = "创建时间", example = "2023-02-01 10:10:10")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;

    @ApiModelProperty(value = "修改时间", example = "2023-02-01 10:10:10")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date updateTime;
}
