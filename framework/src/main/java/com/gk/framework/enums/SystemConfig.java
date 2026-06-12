package com.gk.framework.enums;

import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.YesOrNo;
import com.gk.common.intf.IConfig;
import lombok.Getter;

/**
 * 系统模块配置类
 *
 * @author Flame
 * @since 2024-08-26 15:27
 **/
@Getter
public enum SystemConfig implements IConfig {

    /**
     * 接口加密
     */
    CRYPTO_ENABLE("接口加密", "crypto-enable", YesOrNo.YES.value),

    /**
     * 行为验证码
     */
    CAPTCHA_ENABLE("行为验证码", "captcha-enable", YesOrNo.YES.value),

    /**
     * 用户注册
     */
    REGISTER_ENABLE("用户注册", "register-enable", YesOrNo.NO.value),

    /**
     * WEB前端水印
     */
    WEB_WATERMARK("WEB前端水印", "web-watermark", YesOrNo.NO.value),

    /**
     * 登录失败最大次数
     */
    LOGIN_ERROR_LIMIT("登录失败最大次数", "login-error-limit", "3"),

    /**
     * 登录失败锁定时长
     */
    LOGIN_LOCK_HOUR("登录失败锁定时长", "login-lock-hour", "24"),

    /**
     * 最大导出数量限制
     */
    MAX_EXPORT_LIMIT("最大导出数量限制", "max-export-limit", "150000"),

    /**
     * 导出的单次 SQL 查询返回的最大行数
     */
    EXPORT_QUERY_LIMIT("导出偏移数量", "export-query-limit", "1000"),

    /**
     * 导出的单个 Excel sheet 最大行数
     */
    EXPORT_SHEET_ROWS("导出页签行数", "export-sheet-rows", "400000"),

    /**
     * 文件清理
     */
    FILE_CLEAR("系统文件清理", "file-clear", "90"),

    /**
     * 不适用(占位配置项)
     */
    NA("NA", "NA", CommonConstant.EMPTY);

    /**
     * 数据库 config_name 字段
     */
    public final String name;

    /**
     * 数据库 config_key 字段
     */
    public final String key;

    /**
     * 数据库 config_value 字段
     */
    public final String value;

    SystemConfig(String name, String key, String value) {
        this.name = name;
        this.key = key;
        this.value = value;
    }

}
