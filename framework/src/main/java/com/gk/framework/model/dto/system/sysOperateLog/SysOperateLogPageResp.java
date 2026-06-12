package com.gk.framework.model.dto.system.sysOperateLog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import com.gk.common.utils.StringExtUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 操作日志 分页响应类
 *
 * @author Flame
 * @since 2022-12-19 14:43:46
 */
@ApiModel(value = "操作日志分页响应")
@Data
@Accessors(chain = true)
public class SysOperateLogPageResp {

    @ApiModelProperty(value = "操作日志id", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "部门表id", example = "1000000000000000001")
    private long deptId;

    @ApiModelProperty(value = "操作标题", example = "新增用户")
    private String title;

    @ApiModelProperty(value = "操作Url", example = "/api/sys-role")
    private String requestUrl;

    @ApiModelProperty(value = "操作方式", example = "POST")
    private String requestMethod;

    @ApiModelProperty(value = "操作类型", example = "Add")
    private String type;

    @ApiModelProperty(value = "操作ip", example = "192.168.0.1")
    private String ip;

    @ApiModelProperty(value = "操作状态(1:成功, 0:失败)", example = "1")
    private String status;

    @ApiModelProperty(value = "创建时间", example = "2023-02-01 10:10:10")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;

    @ApiModelProperty(value = "账号", example = "admin")
    private String creatorUsername;

    @ApiModelProperty(value = "姓名", example = "管理员")
    private String creatorNickname;

    public void handleData() {
        this.creatorUsername = StringExtUtils.hideSuperAdmin(creatorUsername);
    }

}
