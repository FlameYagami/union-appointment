package com.gk.framework.model.dto.system.sysDictData;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 字典数据 导出响应类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@ApiModel(value = "字典数据导出响应")
@Data
@Accessors(chain = true)
@HeadRowHeight(20)
public class SysDictDataExportResp {

    @ExcelProperty("字典编码")
    private String dictCode;

    @ExcelProperty("字典标签")
    private String dictLabel;

    @ExcelProperty("字典值")
    private String dictValue;

    @ExcelProperty("颜色类型")
    private String colorType;

    @ExcelProperty("字典样式")
    private String dictCss;

    @ExcelProperty("字典排序")
    private Integer dictOrder;
}
