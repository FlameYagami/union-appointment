package com.gk.framework.model.dto.system.sysDictData;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 字典数据 响应类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@ApiModel(value = "字典数据响应")
@Data
@Accessors(chain = true)
public class SysDictDataResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "字典编码", example = "字典编码")
    private String dictCode;

    @ApiModelProperty(value = "字典标签", example = "字典标签")
    private String dictLabel;

    @ApiModelProperty(value = "字典值", example = "字典值")
    private String dictValue;

    @ApiModelProperty(value = "颜色类型", example = "颜色类型")
    private String colorType;

    @ApiModelProperty(value = "字典样式", example = "字典样式")
    private String dictCss;

    @ApiModelProperty(value = "字典排序", example = "100")
    private int dictOrder;

    @ApiModelProperty(value = "字典数据状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

}
