package com.gk.framework.utils;

import com.gk.common.enums.HtmlType;
import com.gk.framework.constant.GenConstant;
import com.gk.framework.model.dto.generate.GenColumnResp;

/**
 * 前端代码生成工具类
 *
 * @author GuoYu
 * @since 2024-03-19 09:34
 **/
public class GenUtils {

    /**
     * 处理表字段对应的htmlType
     */
    public static void handleHtml(GenColumnResp resp) {
        if (resp.getColumnName().contains("id") && !resp.getColumnName().contains("fid")) {
            return;
        }
        if (GenConstant.COLUMN_TYPE_STRING.contains(resp.getDataType())) {
            // 字符串长度为1或2的, 肯定是存储字典, 默认使用下拉框
            if (resp.getStrLength() == 1 || resp.getStrLength() == 2) {
                resp.setHtmlType(HtmlType.SELECT.value);
                resp.setDictCode(resp.getColumnName());
            } else if (resp.getStrLength() < 500) {
                resp.setHtmlType(HtmlType.INPUT.value);
            } else {
                resp.setHtmlType(HtmlType.TEXTAREA.value);
            }
        } else if (GenConstant.COLUMN_TYPE_TEXT.contains(resp.getDataType())) {
            resp.setHtmlType(HtmlType.EDITOR.value);
        } else if (GenConstant.COLUMN_TYPE_NUMBER.contains(resp.getDataType())) {
            resp.setHtmlType(HtmlType.NUMBER.value);
        } else if (GenConstant.COLUMN_TYPE_TIME.contains(resp.getDataType())) {
            resp.setHtmlType(HtmlType.DATE_PICKER.value);
        } else if (resp.getColumnName().contains("fid")) {
            resp.setHtmlType(HtmlType.FILE_UPLOAD.value);
        }
    }
}
