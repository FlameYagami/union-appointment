package com.gk.framework.model.dto.system.sysLoginLog;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 登录日志 响应类
 *
 * @author Flame
 * @since 2022-12-19 14:43:46
 */
@Data
@Accessors(chain = true)
public class SysLoginLogResp {

    @ApiModelProperty(value = "登录标题", example = "新增用户")
    private String title;

    @ApiModelProperty(value = "登录Url", example = "/api/sys-user")
    private String requestUrl;

    @ApiModelProperty(value = "登录方式", example = "POST")
    private String requestMethod;

    @ApiModelProperty(value = "登录类型", example = "Add")
    private String type;

    @ApiModelProperty(value = "登录参数", example = "[]")
    private String param;

    @ApiModelProperty(value = "登录状态(1:成功, 0:失败)", example = "1")
    private String result;

    @ApiModelProperty(value = "登录ip", example = "192.168.0.1")
    private String ip;

}
