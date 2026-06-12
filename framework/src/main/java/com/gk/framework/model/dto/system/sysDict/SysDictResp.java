package com.gk.framework.model.dto.system.sysDict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 字典 响应类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@ApiModel(value = "字典响应")
@Data
@Accessors(chain = true)
public class SysDictResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "字典名称", example = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典编码", example = "字典编码")
    private String dictCode;

    @ApiModelProperty(value = "字典备注", example = "字典备注")
    private String remark;

}
