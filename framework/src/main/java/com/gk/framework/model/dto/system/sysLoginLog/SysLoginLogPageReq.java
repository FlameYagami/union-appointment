package com.gk.framework.model.dto.system.sysLoginLog;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录日志 分页请求类
 *
 * @author Flame
 * @since 2022-12-19 14:43:46
 */
@ApiModel(value = "登录日志分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLoginLogPageReq extends PageDateReq {

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "登录状态(1:成功, 0:失败)", example = "1")
    private String status;

}
