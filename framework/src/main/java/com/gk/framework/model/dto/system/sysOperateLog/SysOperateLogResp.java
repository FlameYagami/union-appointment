package com.gk.framework.model.dto.system.sysOperateLog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 操作日志 响应类
 *
 * @author Flame
 * @since 2022-12-19 14:43:46
 */
@Data
@Accessors(chain = true)
public class SysOperateLogResp {

    @ApiModelProperty(value = "操作标题", example = "新增用户")
    private String title;

    @ApiModelProperty(value = "操作Url", example = "/api/sys-user")
    private String requestUrl;

    @ApiModelProperty(value = "操作方式", example = "POST")
    private String requestMethod;

    @ApiModelProperty(value = "操作类型", example = "Add")
    private String type;

    @ApiModelProperty(value = "操作参数", example = "[]")
    private String param;

    @ApiModelProperty(value = "操作状态(1:成功, 0:失败)", example = "1")
    private String result;

    @ApiModelProperty(value = "操作ip", example = "192.168.0.1")
    private String ip;

    @ApiModelProperty(value = "操作状态(1:成功, 0:失败)", example = "1")
    private String status;

    @ApiModelProperty(value = "创建时间", example = "2023-02-01 10:10:10")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;

}
