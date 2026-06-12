package com.gk.framework.model.dto.system.sysDict;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典 分页请求类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@ApiModel(value = "字典分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictPageReq extends PageDateReq {

    @ApiModelProperty(value = "字典名称", example = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典编码", example = "字典编码")
    private String dictCode;
}
