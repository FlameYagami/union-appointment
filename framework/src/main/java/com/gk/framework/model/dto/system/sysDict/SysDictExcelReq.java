package com.gk.framework.model.dto.system.sysDict;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.gk.common.model.base.BaseExcelImportReq;
import com.gk.common.model.others.ExcelImportError;
import com.gk.framework.model.entity.system.SysDict;
import com.gk.framework.model.entity.system.SysDictData;
import lombok.Data;

/**
 * 字典 Excel导入请求类
 *
 * @author GuoYu
 * @since 2023-05-05 10:40
 **/
@Data
public class SysDictExcelReq extends BaseExcelImportReq {

    @ExcelProperty(value = "字典名称", index = 0)
    private String dictName;

    @ExcelProperty(value = "字典编码", index = 1)
    private String dictCode;

    @ExcelProperty(value = "字典标签", index = 2)
    private String dictLabel;

    @ExcelProperty(value = "字典值", index = 3)
    private String dictValue;

    @ExcelProperty(value = "字典排序", index = 4)
    private Integer dictOrder;

    public SysDict toDictEntity() {
        return new SysDict()
                .setDictName(dictName)
                .setDictCode(dictCode);
    }

    public SysDictData toDictDataEntity() {
        return new SysDictData()
                .setDictCode(dictCode)
                .setDictLabel(dictLabel)
                .setDictValue(dictValue)
                .setDictOrder(dictOrder);
    }

    @Override
    public ExcelImportError toError(String errorMsg) {
        return new ExcelImportError()
                .setIndexRow(this.indexRow)
                .setDataName("字典编码: " + this.dictCode + ", 字典标签: " + this.dictLabel)
                .setErrorMsg(errorMsg);
    }

    @Override
    public boolean isEmpty() {
        return StrUtil.isEmpty(dictName)
                && StrUtil.isEmpty(dictCode)
                && StrUtil.isEmpty(dictLabel)
                && StrUtil.isEmpty(dictValue)
                && dictOrder != null;
    }
}
