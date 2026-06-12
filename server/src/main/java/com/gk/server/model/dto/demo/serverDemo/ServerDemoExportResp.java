package com.gk.server.model.dto.demo.serverDemo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.gk.common.constant.DateConstant;
import com.gk.framework.annotation.DictFormat;
import com.gk.framework.constant.DictConstant;
import com.gk.framework.convert.DictConvert;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 示例 导出响应类
 *
 * @author GuoYu
 * @since 2023-04-26 16:06:07
 */
@Data
@Accessors(chain = true)
@HeadRowHeight(20)
public class ServerDemoExportResp {

    @ExcelProperty("示例名称")
    private String demoName;

    @ExcelProperty("示例编码")
    private String demoCode;

    @ExcelProperty("示例时间")
    @DateTimeFormat(DateConstant.DEFAULT_PATTERN)
    private Date demoTime;

    @ExcelProperty(value = "数据状态", converter = DictConvert.class)
    @DictFormat(DictConstant.CODE_DATA_STATUS)
    private String dataStatus;

    @ExcelProperty("示例备注")
    private String remark;
}
