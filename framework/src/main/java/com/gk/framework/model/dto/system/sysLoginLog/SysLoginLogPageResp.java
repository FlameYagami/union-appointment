package com.gk.framework.model.dto.system.sysLoginLog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 登录日志 分页响应类
 *
 * @author Flame
 * @since 2022-12-19 14:43:46
 */
@ApiModel(value = "登录日志分页响应")
@Data
@Accessors(chain = true)
public class SysLoginLogPageResp {

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "登录ip", example = "192.168.0.1")
    private String loginIp;

    @ApiModelProperty(value = "浏览器", example = "谷歌")
    private String browser;

    @ApiModelProperty(value = "操作系统", example = "IE")
    private String os;

    @ApiModelProperty(value = "返回消息", example = "登录成功")
    private String resultMsg;

    @ApiModelProperty(value = "登录状态(1:成功, 0:失败)", example = "1")
    private String status;

    @ApiModelProperty(value = "创建时间", example = "2023-02-01 10:10:10")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;

}
