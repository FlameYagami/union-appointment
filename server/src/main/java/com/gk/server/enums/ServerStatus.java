package com.gk.server.enums;

import com.gk.common.intf.IResultStatus;
import lombok.ToString;

/**
 * 业务异常枚举类
 *
 * @author Flame
 */
@ToString
public enum ServerStatus implements IResultStatus {

    // ------------------------ 业务模块 ------------------------
    SERVICE_DEMO_ERROR(100001, "异常枚举示例, 无任何作用, 可以删除"),

    // ------------------------ 场地管理 ------------------------
    VENUE_NOT_FOUND(500001, "场地不存在"),
    VENUE_NOT_ENABLED(500002, "场地未启用"),
    VENUE_NAME_EXIST(500003, "场地名称已存在"),

    // ------------------------ 排期管理 ------------------------
    SCHEDULE_NOT_FOUND(500101, "排期不存在"),
    SCHEDULE_NOT_ENABLED(500102, "排期不存在"),
    SCHEDULE_NOT_OPEN(500103, "该排期暂未开放预约"),
    SCHEDULE_MAX_COUNT_LESS_THAN_BOOKED(500104, "最大预约人数不能小于已预约人数"),
    SCHEDULE_HAS_APPOINTMENT(500105, "该排期已有预约记录，无法删除"),
    SCHEDULE_TIME_ILLEGAL(500106, "排期时间不合法"),

    // ------------------------ 预约管理 ------------------------
    APPOINTMENT_NOT_FOUND(500201, "预约记录不存在"),
    APPOINTMENT_STATUS_NOT_SUPPORT(500202, "当前状态不支持此操作"),
    APPOINTMENT_BLACKLISTED(500203, "您已被列入违约黑名单，暂无法预约"),
    APPOINTMENT_QUOTA_EXCEED(500204, "预约人数超出剩余名额"),
    APPOINTMENT_USER_BLACKLISTED(500205, "该用户已被列入黑名单"),

    // ------------------------ 黑名单管理 ------------------------
    BLACKLIST_NOT_FOUND(500301, "黑名单记录不存在"),
    BLACKLIST_USER_EXIST(500302, "该用户已在黑名单中"),
    BLACKLIST_STATUS_NOT_SUPPORT(500303, "当前状态不支持此操作"),
    BLACKLIST_USER_NOT_FOUND(500304, "用户不存在");


    /**
     * 返回码
     */
    private final int code;

    /**
     * 返回结果描述
     */
    private final String message;

    ServerStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public int getCode() {
        return code;
    }


    @Override
    public String getMessage() {
        return message;
    }


}
