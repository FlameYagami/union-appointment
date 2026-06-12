package com.gk.framework.model.dto.system.sysDictData;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 字典数据 分页请求类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@ApiModel(value = "字典数据分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictDataPageReq extends PageDateReq {

    @ApiModelProperty(value = "字典编码", required = true, example = "字典编码")
    @NotBlank(message = "字典编码不能为空")
    private String dictCode;

    @ApiModelProperty(value = "字典标签", example = "字典标签")
    private String dictLabel;

    @ApiModelProperty(value = "字典值", example = "字典值")
    private String dictValue;
}
