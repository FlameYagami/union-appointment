package com.gk.server.enums;

import com.gk.common.intf.EnumValuable;

/**
 * 黑名单状态枚举
 *
 * @author Codex
 */
public enum BlacklistStatus implements EnumValuable {

    UNBLOCK("0", "解封"),
    BLOCK("1", "封禁");

    public final String value;
    public final String desc;

    BlacklistStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static BlacklistStatus getInstance(String value) {
        if (value == null) {
            return null;
        }
        for (BlacklistStatus status : BlacklistStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}