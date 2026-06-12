package com.gk.security.enums;

/**
 * 行为验证码类型枚举
 *
 * @author Kevin
 * @date 2023-08-11 15:06
 */
public enum CaptchaType {
    /**
     * 滑块拼图
     */
    BLOCK_PUZZLE("blockPuzzle"),
    /**
     * 文字点选
     */
    CLICK_WORD("clickWord");

    public final String value;

    CaptchaType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
