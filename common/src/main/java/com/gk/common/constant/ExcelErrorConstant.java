package com.gk.common.constant;

/**
 * Excel异常常量类
 *
 * @author GuoYu
 * @since 2023-04-26 16:16
 **/
public class ExcelErrorConstant {

    private ExcelErrorConstant() {
        throw new IllegalStateException("Constant class");
    }

    // --------------------------- 通用 ---------------------------

    public static final String DATA_REPEAT_ERROR = "导入的数据中包含重复数据";

    // --------------------------- 字典 ---------------------------

    public static final String DICT_NAME_EMPTY_ERROR   = "必须填写字典名称";
    public static final String DICT_CODE_EMPTY_ERROR   = "必须填写字典编码";
    public static final String DICT_LABEL_EMPTY_ERROR  = "必须填写字典标签";
    public static final String DICT_VALUE_EMPTY_ERROR  = "必须填写字典值";
    public static final String DICT_ORDER_EMPTY_ERROR  = "必须填写字典排序";
    public static final String DICT_CODE_ALREADY_EXIST = "字典编码已存在, 请先删除已有的字典或调整Excel中的数据";

    // --------------------------- 示例 ---------------------------

    public static final String DEMO_NAME_EMPTY_ERROR = "必须填写示例名称";
}
