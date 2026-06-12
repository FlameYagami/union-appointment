package com.gk.common.model.base;

import com.gk.common.model.others.ExcelImportError;

/**
 * Excel 导入基类请求
 *
 * @author Flame
 * @date 2023-05-10 10:00
 **/
public abstract class BaseExcelImportReq {

    /**
     * Excel表的行号
     */
    public int indexRow;

    /**
     * 实现Excel异常信息的构建
     *
     * @param errorMsg 错误描述信息
     * @return Excel错误信息类
     */
    public abstract ExcelImportError toError(String errorMsg);

    /**
     * 实现Excel数据的判空
     *
     * @return 是否为空行
     */
    public abstract boolean isEmpty();

    public void lateInit(int indexRow) {
        this.indexRow = indexRow;
    }

}
