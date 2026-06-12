package com.gk.framework.model.dto.system.sysDict;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 字典 分页响应类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@ApiModel(value = "字典分页响应")
@Data
@Accessors(chain = true)
public class SysDictPageResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "字典名称", example = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典编码", example = "字典编码")
    private String dictCode;

    @ApiModelProperty(value = "字典备注", example = "字典备注")
    private String remark;

    @ApiModelProperty(value = "创建时间", example = "2023-02-01 10:10:10")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;
}
