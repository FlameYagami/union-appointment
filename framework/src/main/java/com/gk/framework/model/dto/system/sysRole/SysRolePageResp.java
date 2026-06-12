package com.gk.framework.model.dto.system.sysRole;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 角色 分页响应类
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */
@ApiModel(value = "角色分页响应")
@Data
@Accessors(chain = true)
public class SysRolePageResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "角色名称", example = "管理员")
    private String roleName;

    @ApiModelProperty(value = "角色编码", example = "admin")
    private String roleCode;

    @ApiModelProperty(value = "角色等级", example = "1")
    private int roleLevel;

    @ApiModelProperty(value = "是否为系统数据(1:是, 0:否, 默认:0)", example = "0")
    private String systemData;

    @ApiModelProperty(value = "角色状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

    @ApiModelProperty(value = "角色排序", example = "100")
    private int roleOrder;

    @ApiModelProperty(value = "创建时间", example = "2022-01-01 00:00:00")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;

}
