package com.gk.server.enums;

import com.gk.common.intf.IResultStatus;
import lombok.ToString;

/**
 * 业务异常枚举类
 *
 * @author Kevin
 * @date 2020-03-23 15:06
 **/
@ToString
public enum ServerStatus implements IResultStatus {

    /**
     * 所有的业务异常返回码统一使用 6位数 以上, 以功能模块来分块管理
     */

    // ------------------------ 业务模块 ------------------------
    SERVICE_DEMO_ERROR(100001, "异常枚举示例, 无任何作用, 可以删除");


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
