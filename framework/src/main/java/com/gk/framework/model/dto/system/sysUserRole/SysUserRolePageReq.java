package com.gk.framework.model.dto.system.sysUserRole;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色用户 分页请求类
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */
@ApiModel(value = "角色用户分页请求")
@Data
public class SysUserRolePageReq extends PageDateReq {

    @ApiModelProperty(value = "角色id", required = true, example = "1000000000000000001")
    private long roleId;

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "姓名", example = "管理员")
    private String nickname;

}
