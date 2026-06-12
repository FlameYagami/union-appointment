package com.gk.server.model.dto.demo.serverDemo;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.gk.common.constant.DateConstant;
import com.gk.common.model.base.BaseExcelImportReq;
import com.gk.common.model.others.ExcelImportError;
import com.gk.framework.annotation.DictFormat;
import com.gk.framework.constant.DictConstant;
import com.gk.framework.convert.DictConvert;
import com.gk.server.model.entity.demo.ServerDemo;
import lombok.Data;

import java.util.Date;

/**
 * 示例 Excel导入请求类
 *
 * @author GuoYu
 * @since 2023-04-26 15:44
 **/
@Data
// 注意: 千万不能使用以下注解, 会导致导入的ExcelReq对象的属性全部为null
//@Accessors(chain = true)
public class ServerDemoExcelReq extends BaseExcelImportReq {

    // 注意：序号可以不用写了，框架会自动读取Excel的行号
//    @ExcelProperty(value = "序号", index = 0)
//    private String indexNum;

    @ExcelProperty(value = "示例名称", index = 0)
    private String demoName;

    @ExcelProperty(value = "示例编码", index = 1)
    private String demoCode;

    @ExcelProperty(value = "示例时间", index = 2)
    @DateTimeFormat(DateConstant.DATE_PATTERN)
    private Date demoTime;

    @ExcelProperty(value = "数据状态", index = 3, converter = DictConvert.class)
    @DictFormat(DictConstant.CODE_DATA_STATUS)
    private String dataStatus;

    @ExcelProperty(value = "示例备注", index = 4)
    private String remark;

    /**
     * 转换为Entity
     */
    public ServerDemo toEntity() {
        return new ServerDemo()
                .setDemoName(demoName)
                .setDemoCode(demoCode)
                .setDemoTime(demoTime)
                .setDataStatus(dataStatus)
                .setRemark(remark);
    }

    @Override
    public ExcelImportError toError(String errorMsg) {
        return new ExcelImportError()
                .setIndexRow(this.indexRow)
                .setDataName(this.demoName)
                .setErrorMsg(errorMsg);
    }

    @Override
    public boolean isEmpty() {
        return StrUtil.isEmpty(demoName)
                && StrUtil.isEmpty(demoCode)
                && demoTime == null
                && StrUtil.isEmpty(dataStatus)
                && StrUtil.isEmpty(remark);
    }
}
