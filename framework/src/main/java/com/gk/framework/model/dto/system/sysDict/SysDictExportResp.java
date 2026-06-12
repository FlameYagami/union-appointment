package com.gk.framework.model.dto.system.sysDict;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.gk.common.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 字典 导出响应类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@ApiModel(value = "字典导出响应")
@Data
@Accessors(chain = true)
@HeadRowHeight(20)
public class SysDictExportResp {

    @ExcelProperty("字典名称")
    private String dictName;

    @ExcelProperty("字典编码")
    private String dictCode;

    @ExcelProperty("字典备注")
    private String remark;

    @ExcelProperty("创建时间")
    @DateTimeFormat(DateConstant.DATE_PATTERN)
    private Date createTime;
}
