package com.gk.common.utils;

import cn.hutool.core.util.DesensitizedUtil;
import com.gk.common.enums.CardType;

/**
 * 脱敏工具类
 *
 * @author GuoYu
 * @since 2023-05-18 11:42
 **/
public class DesensitizedUtils {

    private DesensitizedUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 证件号脱敏
     *
     * @param cardType 证件类型
     * @param cardNumber 证件号
     */
    public static String cardNumber(String cardType, String cardNumber) {
        if (CardType.ID_CARD.value.equals(cardType)) {
            return DesensitizedUtil.idCardNum(cardNumber, 4, 4);
        }
        return DesensitizedUtil.idCardNum(cardNumber, 3, 3);
    }
}
