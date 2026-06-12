package com.gk.framework.model.dto.system.sysRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色标签响应类
 *
 * @author Flame
 * @since 2024-05-31 16:17
 **/
@ApiModel("角色标签响应")
@Data
@Accessors(chain = true)
public class SysRoleLabelResp {

    @ApiModelProperty(value = "标签id", example = "1")
    private long id;

    @ApiModelProperty(value = "标签名称", example = "1")
    private String name;

    @ApiModelProperty(value = "节点是否禁用", example = "false")
    private boolean disabled;

    @ApiModelProperty(value = "角色编码", example = "admin")
    private String roleCode;

}
