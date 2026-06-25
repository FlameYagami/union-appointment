package com.gk.common.constant;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * 通用常量类
 *
 * @author Kevin
 * @since 2020-03-12 10:26
 **/

public class CommonConstant {

    private CommonConstant() {
        throw new IllegalStateException("Constant class");
    }

    /**
     * 默认AES Key
     */
    public static final String DEFAULT_AES_KEY = "JgWusXvC5fbofEac";

    /**
     * 默认AES iv
     */
    public static final String DEFAULT_AES_IV = "E5qyLpVMwTF2nzw4";

    /**
     * 请求头Token令牌的Key
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 签名请求头
     */
    public static final String SIGN_HEADER = "sign";

    /**
     * 认证请求头
     */
    public static final String OPEN_ID_HEADER = "open-id";

    /**
     * 数据的属性名
     */
    public static final String DATA_PARAM = "data";

    /**
     * 数量的属性名
     */
    public static final String TOTAL_PARAM = "total";

    /**
     * 缩略图文件夹
     */
    public static final String THUMB_DIR = "thumb/";

    // --------------------------- 默认id ---------------------------

    /**
     * 顶级虚拟id(机构、菜单表中顶级的parentId)
     */
    public static final long TOP_VIRTUAL_ID = 1000000000000000000L;

    /**
     * 系统表中的顶级id
     */
    public static final long TOP_ID = 1000000000000000001L;

    /**
     * 匿名id
     */
    public static final long ANONYMOUS_ID = 1000000000000000099L;

    /**
     * 居民虚拟id
     */
    public static final long PEOPLE_VIRTUAL_ID = 1000000000000000100L;

    /**
     * 超级管理员账号
     */
    public static final String SUPER_ADMIN_USERNAME = "alpha";

    /**
     * 匿名账号
     */
    public static final String ANONYMOUS_USERNAME = "anonymous";

    /**
     * 居民账号
     */
    public static final String PEOPLE_USERNAME = "people";

    /**
     * 超级管理员权限
     */
    public static final String SUPER_ADMIN_PERMISSION = "*:*:*";

    /**
     * 顶级部门等级
     */
    public static final int TOP_DEPT_LEVEL = 1;

    /**
     * 分页数据数量最大限制数, 为了防止他人爬取大量数据, 也为了防止单页数据加载过慢而限制
     */
    public static final int PAGE_SIZE_LIMIT = 100;

    /**
     * 分页数据默认条数
     */
    public static final int DEFAULT_PAGE_SIZE = 15;

    /**
     * 不适用的数值
     */
    public static final long NA_VALUE = 0L;

    /**
     * 无内容
     */
    public static final String NO_CONTENT = "-";

    /**
     * 无内容(中文)
     */
    public static final String CN_NO_CONTENT = "无";

    /**
     * 未知(中文)
     */
    public static final String CN_UNKNOWN_CONTENT = "未知";

    /**
     * 最小日期
     */
    public static final Date MIN_DATE = DateUtil.parse("0001-01-01", DateConstant.DATE_PATTERN);

    /**
     * 最大日期
     */
    public static final Date MAX_DATE = DateUtil.parse("9999-12-31", DateConstant.DATE_PATTERN);

    // --------------------------- 文件后缀 ---------------------------

    /**
     * EXCEL文件后缀 xls
     */
    public static final String EXT_EXCEL_XLS = "xls";

    /**
     * EXCEL文件后缀 xlsx
     */
    public static final String EXT_EXCEL_XLSX = "xlsx";

    /**
     * WORD文件后缀 doc
     */
    public static final String EXT_WORD_DOC = "doc";

    /**
     * WORD文件后缀 docx
     */
    public static final String EXT_WORD_DOCX = "docx";

    /**
     * PDF文件后缀 pdf
     */
    public static final String EXT_PDF = "pdf";


    // --------------------------- 符号 ---------------------------

    /**
     * 空字符串
     */
    public static final String EMPTY = "";
    /**
     * 点号
     */
    public static final String DOT = ".";
    /**
     * 逗号
     */
    public static final String COMMA = ",";
    /**
     * 单引号
     */
    public static final String APOSTROPHE = "'";
    /**
     * 等号
     */
    public static final String EQUALS = "=";
    /**
     * 与符号
     */
    public static final String AMPERSAND = "&";
    /**
     * 冒号
     */
    public static final String COLON = ":";
    /**
     * 星号
     */
    public static final String ASTERISK = "*";
    /**
     * at符
     */
    public static final String AT = "@";
    /**
     * 井号
     */
    public static final String HASH = "#";
    /**
     * 百分号
     */
    public static final String PERCENT = "%";
    /**
     * 美元符
     */
    public static final String DOLLAR = "$";
    /**
     * 插字符
     */
    public static final String CARET = "^";
    /**
     * 空格
     */
    public static final String SPACE = " ";
    /**
     * 加号
     */
    public static final String PLUS = "+";
    /**
     * 减号
     */
    public static final String MINUS = "-";
    /**
     * 下划线
     */
    public static final String UNDERLINE = "_";
    /**
     * 竖线
     */
    public static final String PIPE = "|";
    /**
     * 斜杠
     */
    public static final String SLASH = "/";
    /**
     * 反斜杠
     */
    public static final String BACK_SLASH = "\\";
    /**
     * 换行
     */
    public static final String NEWLINE = "\r\n";
    /**
     * 左括号
     */
    public static final String LEFT_BRACKETS = "(";
    /**
     * 右括号
     */
    public static final String RIGHT_BRACKETS = ")";

    /**
     * 中文逗号
     */
    public static final String CN_COMMA = "，";
    /**
     * 中文句号
     */
    public static final String CN_STOP = "。";
    /**
     * 顿号
     */
    public static final String CN_PAUSE_MARK = "、";
    /**
     * 中文破折号
     */
    public static final String CN_DASH = "——";
    /**
     * 人民币符号
     */
    public static final String CN_MONEY = "￥";
    /**
     * 中文感叹号
     */
    public static final String CN_EXCLAMATION = "！";
    /**
     * 中文省略号
     */
    public static final String CN_ELLIPSES = "……";

    /**
     * 中文左括号
     */
    public static final String CN_LEFT_BRACKETS = "（";
    /**
     * 中文右括号
     */
    public static final String CN_RIGHT_BRACKETS = "）";
    /**
     * 中文 左书名号
     */
    public static final String CN_LEFT_BOOK_TITLE = "《";
    /**
     * 中文 右书名号
     */
    public static final String CN_RIGHT_BOOK_TITLE = "》";

    /**
     * 数据拆分符号
     */
    public static final String DATA_SEPARATOR_MULTI = "#@#@#";

}
