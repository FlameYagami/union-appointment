package com.gk.server.enums;

import com.gk.common.intf.EnumValuable;

/**
 * 预约审批结果枚举
 *
 * @author Codex
 */
public enum AppointmentApproveResult implements EnumValuable {

    APPROVED(AppointmentStatus.APPROVED.value, "通过"),
    REJECTED(AppointmentStatus.REJECTED.value, "拒绝");

    public final String value;
    public final String desc;

    AppointmentApproveResult(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return value;
    }
}