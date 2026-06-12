package com.gk.framework.model.dto.system.sysDictData;

import com.gk.common.enums.DataStatus;
import com.gk.framework.model.entity.system.SysDictData;
import com.gk.framework.validate.InEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 字典数据 基础请求类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@Data
public class SysDictDataBaseReq {

    @ApiModelProperty(value = "字典编码[64]", required = true, example = "字典编码")
    @NotBlank(message = "字典编码不能为空")
    @Length(max = 64, message = "字典编码最多输入64个字符")
    private String dictCode;

    @ApiModelProperty(value = "字典标签[32]", required = true, example = "字典标签")
    @NotBlank(message = "字典标签不能为空")
    @Length(max = 32, message = "字典标签最多输入32个字符")
    private String dictLabel;

    @ApiModelProperty(value = "字典值[32]", required = true, example = "字典值")
    @NotBlank(message = "字典值不能为空")
    @Length(max = 32, message = "字典值最多输入32个字符")
    private String dictValue;

    @ApiModelProperty(value = "颜色类型[32]", required = true, example = "颜色类型")
    @NotBlank(message = "颜色类型不能为空")
    @Length(max = 32, message = "颜色类型最多输入32个字符")
    private String colorType;

    @ApiModelProperty(value = "字典样式[255]", example = "字典样式")
    @Length(max = 255, message = "字典样式最多输入255个字符")
    private String dictCss;

    @ApiModelProperty(value = "字典排序", required = true, example = "100")
    @Min(value = 1, message = "字典排序不合法")
    private int dictOrder;

    @ApiModelProperty(value = "字典数据状态;(1:正常, 0:停用)", required = true, example = "1")
    @NotBlank(message = "字典数据状态不能为空")
    @InEnum(DataStatus.class)
    private String dataStatus;

    /**
    * 转换成数据库操作类
    */
    protected SysDictData toEntity() {
        return new SysDictData()
                .setDictCode(dictCode)
                .setDictLabel(dictLabel)
                .setDictValue(dictValue)
                .setColorType(colorType)
                .setDictCss(dictCss)
                .setDictOrder(dictOrder)
                .setDataStatus(dataStatus);
    }

}
