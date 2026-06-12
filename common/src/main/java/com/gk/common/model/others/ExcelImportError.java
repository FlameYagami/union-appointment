package com.gk.common.model.others;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Excel 导入异常信息类
 *
 * @author GuoYu
 * @since 2023-04-14 10:06
 **/
@Data
@Accessors(chain = true)
public class ExcelImportError {

    /**
     * 导入的序号
     */
    private int indexRow;

    /**
     * 导入数据的提示名称
     */
    private String dataName;

    /**
     * 导入数据的异常描述
     */
    private String errorMsg;

}
