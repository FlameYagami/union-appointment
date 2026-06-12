package com.gk.framework.model.dto.system.sysUserRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 角色中用户 删除请求类
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */
@ApiModel(value = "角色中用户删除请求")
@Data
public class SysUserRoleDeleteReq {

    @ApiModelProperty(value = "角色id", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000002L, message = "id不合法") // 超管角色校验
    private long roleId;

    @ApiModelProperty(value = "角色id", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000002L, message = "id不合法") // 超管角色校验
    private long userId;

}
