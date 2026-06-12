package com.gk.server.model.others;

import com.gk.common.enums.SysStatus;
import com.gk.common.intf.IResultStatus;
import com.gk.common.model.others.BaseResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 接口结果加密封装类
 *
 * @author GuoYu
 * @since 2023-03-10 15:28
 **/

@ApiModel(description = "接口加密结果")
@Data
public class EncryptPageResult extends BaseResult {

    @ApiModelProperty(value = "数据", example = "{}")
    private String data;

    @ApiModelProperty(value = "总量", example = "1")
    private int total;

    @ApiModelProperty(value = "加密", example = "1")
    private String encrypt;

    public EncryptPageResult(IResultStatus status, String data, int total) {
        super(status);
        this.data = data;
        this.total = total;
        this.encrypt = "1";
    }

    /**
     * 业务成功
     */
    public static EncryptPageResult ok(String data, int total) {
        return new EncryptPageResult(SysStatus.SUCCESS, data, total);
    }
}
