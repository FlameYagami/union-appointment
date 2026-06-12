package com.gk.framework.constant;

import java.util.List;

/**
 * 前端代码生成 常量类
 *
 * @author GuoYu
 * @since 2024-03-19 09:03
 **/
public class GenConstant {

    /** 数据库字符串类型 */
    public static final List<String> COLUMN_TYPE_STRING = List.of("char", "varchar", "nvarchar", "varchar2", "tinytext");

    /** 数据库文本类型 */
    public static final List<String> COLUMN_TYPE_TEXT = List.of("text", "mediumtext", "longtext", "blob", "clob");

    /** 数据库时间类型 */
    public static final List<String> COLUMN_TYPE_TIME = List.of("datetime", "time", "date", "timestamp");

    /** 数据库数字类型 */
    public static final List<String> COLUMN_TYPE_NUMBER = List.of("tinyint", "smallint", "mediumint", "int",
            "number", "integer", "bit", "bigint", "float", "double", "decimal");
}
