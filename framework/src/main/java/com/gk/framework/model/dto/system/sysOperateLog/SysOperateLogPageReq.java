package com.gk.framework.model.dto.system.sysOperateLog;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 操作日志 分页请求类
 *
 * @author Flame
 * @since 2022-12-19 14:43:46
 */
@ApiModel(value = "操作日志分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOperateLogPageReq extends PageDateReq {

    @ApiModelProperty(value = "操作人", example = "管理员")
    private String creator;

    @ApiModelProperty(value = "操作标题", example = "新增用户")
    private String title;

    @ApiModelProperty(value = "操作Url", example = "/api/sys-role")
    private String requestUrl;

    @ApiModelProperty(value = "操作方式", example = "POST")
    private String requestMethod;

    @ApiModelProperty(value = "操作类型", example = "Update")
    private String type;

    @ApiModelProperty(value = "操作状态(1:成功, 0:失败)", example = "1")
    private String status;

}
