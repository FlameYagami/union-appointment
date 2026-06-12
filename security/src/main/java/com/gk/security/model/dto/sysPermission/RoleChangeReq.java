package com.gk.security.model.dto.sysPermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 角色切换 请求类
 *
 * @author Flame
 * @date 2023-01-31 16:08
 **/
@ApiModel(value = "角色切换请求")
@Data
public class RoleChangeReq {

    @ApiModelProperty(value = "角色id", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000002L, message = "id不合法") // 超管角色校验
    private long roleId;

}
