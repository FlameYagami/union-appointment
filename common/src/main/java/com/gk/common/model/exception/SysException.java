package com.gk.common.model.exception;

import com.gk.common.enums.SysStatus;
import com.gk.common.intf.IResultStatus;
import lombok.Data;

/**
 * 系统异常类
 *
 * @author Flame
 * @date 2022-07-15 14:41
 */

@Data
public class SysException extends RuntimeException {

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 报错数据
     */
    private Object data;


    /**
     * 通用异常构造方法
     */
    public SysException(String message) {
        super(message, null, false, false);
        this.code = SysStatus.FAILED.getCode();
        this.message = message;
    }

    public SysException(int code, String message) {
        super(message, null, false, false);
        this.code = code;
        this.message = message;
    }

    public SysException(IResultStatus status) {
        super(status.getMessage(), null, false, false);
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public <T> SysException(IResultStatus resultStatus, T data) {
        super(resultStatus.getMessage(), null, false, false);
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
        this.data = data;
    }

}
