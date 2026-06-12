package com.gk.server.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * 业务类型
 *
 * @author Flame
 * @date 2023-02-28 10:02
 **/
@Getter
public enum BizType implements EnumValuable {

    /**
     * 用户头像
     */
    AVATAR("1"),

    /**
     * 公告图片
     */
    NOTICE("2");

    public final String value;

    BizType(String value) {
        this.value = value;
    }

}
