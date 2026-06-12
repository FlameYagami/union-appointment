package com.gk.framework.constant;

/**
 * Redis缓存常量类
 *
 * @author GuoYu
 * @since 2023-03-13 17:15
 **/
public class RedisConstant {

    private RedisConstant() {
        throw new IllegalStateException("Constant class");
    }

    // --------------------------- Redis Key ---------------------------

    /**
     * 系统登录用户
     */
    public static final String LOGIN_TOKEN_KEY = "login_token:";

    /**
     * 存放用户登录失败的次数
     */
    public static final String LOGIN_ERROR_KEY = "login_error:";

    /**
     * 用户信息
     */
    public static final String USER_INFO_KEY = "user_info:";

    /**
     * 字典信息(key: dictCode, value: dictData)
     */
    public static final String DICT_DATA_KEY = "dict_data:";

    /**
     * 配置信息(key: configKey, value: configValue)
     */
    public static final String CONFIG_KEY = "sys_config:";

    /**
     * 防重提交
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 最后的用户
     */
    public static final String LAST_USER_KEY = "last_user:";

    /**
     * 最后的用户角色
     */
    public static final String LAST_ROLE_KEY = "last_role:";

    /**
     * 最后的用户部门
     */
    public static final String LAST_DEPT_KEY = "last_dept:";

    /**
     * 第三方对接配置
     */
    public static final String OPEN_CONFIG_KEY = "open_config:";

    // --------------------------- Redis Hash Key ---------------------------

    /**
     * Redis中Token令牌的hashKey
     */
    public static final String TOKEN_HASH_KEY = "token";
    /**
     * Redis中Token令牌过期时间戳的hashKey
     */
    public static final String EXPIRED_HASH_KEY = "expired";
    /**
     * Redis中当前角色ID的hashKey
     */
    public static final String ROLE_HASH_KEY = "current_role";
    /**
     * Redis中当前角色编码的hashKey
     */
    public static final String ROLE_CODE_HASH_KEY = "current_role_code";
    /**
     * Redis中当前部门ID的hashKey
     */
    public static final String DEPT_HASH_KEY = "current_dept";
    /**
     * Redis中AES的密钥
     */
    public static final String AES_KEY_HASH_KEY = "aes_key";
    /**
     * Redis中AES的iv
     */
    public static final String AES_IV_HASH_KEY = "aes_iv";
}
