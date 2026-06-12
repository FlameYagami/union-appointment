package com.gk.common.model.others;

import com.gk.common.intf.IResultStatus;
import com.gk.common.enums.SysStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 框架封装 响应类
 *
 * @author Flame
 * @date 2022-11-29 16:34
 */

@ApiModel(description = "基础级回复")
@Data
public class BaseResult {

    @ApiModelProperty(value = "状态码", example = "1")
    private int code;

    @ApiModelProperty(value = "描述", example = "请求成功")
    private String msg;

    protected BaseResult() {
    }

    protected BaseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    protected BaseResult(IResultStatus status) {
        this.code = status.getCode();
        this.msg = status.getMessage();
    }

    public static BaseResult ok() {
        return new BaseResult(SysStatus.SUCCESS);
    }

    public static BaseResult error(String resultDesc) {
        return new BaseResult(SysStatus.FAILED.getCode(), resultDesc);
    }

    public static BaseResult error(IResultStatus status) {
        return new BaseResult(status);
    }

    public static BaseResult error(int resultCode, String resultDesc) {
        return new BaseResult(resultCode, resultDesc);
    }


}
