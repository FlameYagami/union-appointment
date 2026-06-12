package com.gk.security.enums;

/**
 * 图片类型枚举
 *
 * @author Kevin
 * @date 2023-08-11 15:06
 */
public enum CaptchaImage {
    /**
     * 滑动验证码背景图
     */
    SLIDE_BACKGROUND("SLIDE_BACKGROUND"),
    /**
     * 滑动验证码滑块图片
     */
    SLIDE_BLOCK("SLIDE_BLOCK"),
    /**
     * 点选文字验证码背景图
     */
    CLICK_WORD_BACKGROUND("CLICK_WORD_BACKGROUND");

    public final String value;

    CaptchaImage(String value) {
        this.value = value;
    }
}
