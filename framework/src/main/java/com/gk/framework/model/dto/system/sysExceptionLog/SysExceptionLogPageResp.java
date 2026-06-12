package com.gk.framework.model.dto.system.sysExceptionLog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 异常日志 分页响应类
 *
 * @author Flame
 * @since 2024-04-08 10:49:10
 */
@ApiModel(value = "异常日志分页响应")
@Data
@Accessors(chain = true)
public class SysExceptionLogPageResp {

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "异常类型(1:暴力登录, 2:异地登录, 3:账号锁定, 4:密码变更)", example = "1")
    private String exceptionType;

    @ApiModelProperty(value = "ip地址", example = "192.168.0.1")
    private String ip;

    @ApiModelProperty(value = "ip归属地", example = "中国|江西省|南昌")
    private String location;

    @ApiModelProperty(value = "浏览器", example = "谷歌")
    private String browser;

    @ApiModelProperty(value = "操作系统", example = "IE")
    private String os;

    @ApiModelProperty(value = "创建时间", example = "2023-02-01 10:10:10")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;

    @ApiModelProperty(value = "用户id(username可能在系统不存在, 此字段可能为空)", example = "1000000000000000001")
    private Long userId;

    @ApiModelProperty(value = "黑名单状态(1:是, 0:否)", example = "1")
    private String blacklistStatus;

}
