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
 * @since 2023-03-10 10:41
 **/

@ApiModel(description = "接口加密结果")
@Data
public class EncryptApiResult extends BaseResult {

    @ApiModelProperty(value = "数据", example = "{}")
    private String data;

    @ApiModelProperty(value = "加密", example = "1")
    private String encrypt;

    public EncryptApiResult(IResultStatus status, String data) {
        super(status);
        this.data = data;
        // 加密结果的统一附加参数
        this.encrypt = "1";
    }

    /**
     * 业务成功
     */
    public static EncryptApiResult ok(String data) {
        return new EncryptApiResult(SysStatus.SUCCESS, data);
    }
}
