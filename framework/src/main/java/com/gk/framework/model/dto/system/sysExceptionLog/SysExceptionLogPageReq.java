package com.gk.framework.model.dto.system.sysExceptionLog;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 异常日志 分页请求类
 *
 * @author GuoYu
 * @since 2024-05-15 15:16:18
 */
@ApiModel(value = "异常日志分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysExceptionLogPageReq extends PageDateReq {

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "异常类型(1:暴力登录, 2:异地登录, 3:账号锁定, 4:密码变更)", example = "1")
    private String exceptionType;

    @ApiModelProperty(value = "ip地址", example = "ip地址")
    private String ip;

    @ApiModelProperty(value = "ip归属地", example = "ip归属地")
    private String location;

    @ApiModelProperty(value = "浏览器", example = "浏览器")
    private String browser;

    @ApiModelProperty(value = "操作系统", example = "操作系统")
    private String os;
}
