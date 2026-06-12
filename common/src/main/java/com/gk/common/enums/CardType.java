package com.gk.common.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * @author GuoYu
 * @since 2023-05-18 14:34
 **/
@Getter
public enum CardType implements EnumValuable {

    /** 身份证 */
    ID_CARD("01"),
    /** 护照 */
    PASSPORT("03"),
    /** 其他 */
    OTHER("99");

    public final String value;

    CardType(String value) {
        this.value = value;
    }
}
