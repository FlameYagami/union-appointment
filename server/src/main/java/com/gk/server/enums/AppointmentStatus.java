package com.gk.server.enums;

import com.gk.common.intf.EnumValuable;

/**
 * 预约状态枚举
 *
 * @author Codex
 */
public enum AppointmentStatus implements EnumValuable {

    PENDING("pending", "待审批"),
    APPROVED("approved", "已通过"),
    REJECTED("rejected", "已拒绝"),
    CANCELLED("cancelled", "已取消"),
    COMPLETED("completed", "已完成"),
    VIOLATED("violated", "违约");

    public final String value;
    public final String desc;

    AppointmentStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static AppointmentStatus getInstance(String value) {
        if (value == null) {
            return null;
        }
        for (AppointmentStatus status : AppointmentStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}