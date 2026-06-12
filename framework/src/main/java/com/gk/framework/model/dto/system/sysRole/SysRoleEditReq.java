package com.gk.framework.model.dto.system.sysRole;

import com.gk.framework.model.entity.system.SysRole;
import com.gk.framework.model.entity.system.SysRoleMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * 角色 修改请求类
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */
@ApiModel(value = "角色修改请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleEditReq extends SysRoleBaseReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000002L, message = "id不合法") // 超管角色校验
    private long id;

    @Override
    public SysRole toEntity() {
        return super.toEntity()
                .setId(id);
    }

    @Override
    public List<SysRoleMenu> toSysRoleMenus(long roleId) {
        return super.toSysRoleMenus(roleId);
    }

}
