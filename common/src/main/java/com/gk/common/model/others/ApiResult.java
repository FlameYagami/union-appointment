package com.gk.common.model.others;

import com.gk.common.enums.SysStatus;
import com.gk.common.intf.IResultStatus;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 接口结果封装类
 *
 * @author Kevin
 * @date 2020-03-23 15:07
 **/

@ApiModel(description = "接口结果")
@Data
public class ApiResult<T> extends BaseResult {

    /**
     * 数据
     */
    private T data;

    private ApiResult(IResultStatus status, T data) {
        super(status.getCode(), status.getMessage());
        this.data = data;
    }

    private ApiResult(int resultCode, String resultDesc, T data) {
        super(resultCode, resultDesc);
        this.data = data;
    }

    /**
     * 业务结果
     */
    public static ApiResult<?> result(IResultStatus resultStatus) {
        return new ApiResult<>(resultStatus, null);
    }

    /**
     * 业务结果
     */
    public static <T> ApiResult<T> result(IResultStatus resultStatus, T data) {
        return new ApiResult<>(resultStatus, data);
    }

    /**
     * 业务成功
     */
    public static ApiResult<?> ok() {
        return new ApiResult<>(SysStatus.SUCCESS, null);
    }

    /**
     * 业务成功
     */
    public static <T> ApiResult<T> ok(T data) {
        return new ApiResult<>(SysStatus.SUCCESS, data);
    }


    /**
     * 业务错误
     */
    public static ApiResult<?> error(int resultCode, String resultDesc) {
        return new ApiResult<>(resultCode, resultDesc, null);
    }

    /**
     * 业务错误
     */
    public static <T> ApiResult<T> error(int resultCode, String resultDesc, T data) {
        return new ApiResult<>(resultCode, resultDesc, data);
    }

    /**
     * 业务错误
     */
    public static ApiResult<?> error(IResultStatus resultStatus) {
        return new ApiResult<>(resultStatus.getCode(), resultStatus.getMessage(), null);
    }

    /**
     * 业务错误
     */
    public static <T> ApiResult<T> error(IResultStatus resultStatus, T data) {
        return new ApiResult<>(resultStatus.getCode(), resultStatus.getMessage(), data);
    }

}
