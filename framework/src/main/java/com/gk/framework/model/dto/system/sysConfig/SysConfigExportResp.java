package com.gk.framework.model.dto.system.sysConfig;

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
 * 配置 导出响应类
 *
 * @author GuoYu
 * @since 2023-03-06 14:56:24
 */
@Data
@Accessors(chain = true)
@HeadRowHeight(20)
public class SysConfigExportResp {

    @ExcelProperty("配置名称")
    private String configName;

    @ExcelProperty("配置键")
    private String configKey;

    @ExcelProperty("配置值")
    private String configValue;

    @ExcelProperty(value = "配置状态", converter = DictConvert.class)
    @DictFormat(DictConstant.CODE_DATA_STATUS)
    private String dataStatus;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    @DateTimeFormat(DateConstant.DATE_PATTERN)
    private Date createTime;
}
