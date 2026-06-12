package com.gk.framework.model.dto.system.sysRole;

import com.gk.framework.model.entity.system.SysRole;
import com.gk.framework.model.entity.system.SysRoleMenu;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色 新增请求类
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */
@ApiModel(value = "角色新增请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleAddReq extends SysRoleBaseReq {

    @Override
    public SysRole toEntity() {
        return super.toEntity();
    }

    /**
     * 转换成数据库操作类
     */
    @Override
    public List<SysRoleMenu> toSysRoleMenus(long roleId) {
        return super.toSysRoleMenus(roleId);
    }

}
