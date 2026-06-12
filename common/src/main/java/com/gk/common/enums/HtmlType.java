package com.gk.common.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * 前端代码生成HTML类型枚举类
 *
 * @author GuoYu
 * @since 2024-03-23 09:55
 **/
@Getter
public enum HtmlType implements EnumValuable {
    /** 文本框 */
    INPUT("input"),
    /** 数字框 */
    NUMBER("number"),
    /** 文本域 */
    TEXTAREA("textarea"),
    /** 下拉框 */
    SELECT("select"),
    /** 单选框 */
    RADIO("radio"),
    /** 复选框 */
    CHECKBOX("checkbox"),
    /** 日期控件 */
    DATE_PICKER("datetime"),
    /** 上传控件 */
    FILE_UPLOAD("fileUpload"),
    /** 图片上传控件 */
    IMAGE_UPLOAD("imageUpload"),
    /** 富文本域 */
    EDITOR("editor");

    public final String value;

    HtmlType(String value) {
        this.value = value;
    }
}
