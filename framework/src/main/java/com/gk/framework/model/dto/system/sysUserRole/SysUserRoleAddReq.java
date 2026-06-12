package com.gk.framework.model.dto.system.sysUserRole;

import com.gk.framework.model.entity.system.SysUserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色中用户 添加请求类
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */
@ApiModel(value = "角色中用户添加请求")
@Data
public class SysUserRoleAddReq {

    @ApiModelProperty(value = "角色id", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000002L, message = "id不合法") // 超管角色校验
    private long roleId;

    @ApiModelProperty(value = "用户id集合", required = true, example = "[1000000000000000001]")
    @NotNull(message = "用户id集合缺失")
    @Size(max = 100, message = "选择的用户不能超过100个")
    private List<@Min(value = 1000000000000000002L, message = "id不合法") Long> userIds;

    public List<SysUserRole> toSysUserRoles() {
        return userIds.stream()
                .map(userId -> new SysUserRole()
                        .setUserId(userId)
                        .setRoleId(roleId)
                ).collect(Collectors.toList());
    }

}
