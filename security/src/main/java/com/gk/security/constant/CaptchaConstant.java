package com.gk.security.constant;

/**
 * 行为验证码常量类
 *
 * @author GuoYu
 * @since 2023-08-14 10:53
 **/
public class CaptchaConstant {

    /**
     * PNG图片类型
     */
    public static final String IMAGE_TYPE_PNG = "png";
    /**
     * 汉字字体
     */
    public static final int HAN_ZI_SIZE = 25;
    /**
     * 汉字半字体
     */
    public static final int HAN_ZI_HALF_SIZE = HAN_ZI_SIZE / 2;
    /**
     * Redis check校验坐标的Key
     */
    public static final String CAPTCHA_CHECK_KEY = "captcha:check-%s";
    /**
     * Redis verify校验坐标的Key
     */
    public static final String CAPTCHA_VERIFY_KEY = "captcha:verify-%s";
    /**
     * Redis 请求限制次数的key
     */
    public static final String CAPTCHA_LIMIT_KEY = "captcha:req_limit-%s-%s";
    /**
     * 密钥包含两个信息, AES key 及 AES iv, 用 | 分隔
     */
    public static final int SECRET_KEY_SIZE = 2;
    /**
     * 后台验证码位置校验的信息连接符
     */
    public static final String VERIFY_SEPARATOR = "---";

    /**
     * 接口限制的Redis缓存时长
     */
    public static final Long EXPIRE_LIMIT_DURATION = 60L;
    /**
     * check校验的Redis缓存时长
     */
    public static final Long EXPIRE_CHECK_DURATION = 2 * 60L;
    /**
     * verify校验的Redis缓存时长
     */
    public static final Long EXPIRE_VERIFY_DURATION = 3 * 60L;
}
