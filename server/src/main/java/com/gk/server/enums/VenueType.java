package com.gk.server.enums;

import com.gk.common.intf.EnumValuable;

/**
 * 场地类型枚举
 *
 * @author Codex
 */
public enum VenueType implements EnumValuable {

    VENUE("venue", "场地"),
    COURSE("course", "课程"),
    ACTIVITY("activity", "活动");

    public final String value;
    public final String desc;

    VenueType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static VenueType getInstance(String value) {
        if (value == null) {
            return null;
        }
        for (VenueType type : VenueType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}