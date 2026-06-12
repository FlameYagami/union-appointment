package com.gk.framework.model.bo.role;

import lombok.Data;

/**
 * 角色权限
 *
 * @author Flame
 * @date 2022-12-29 16:16
 **/
@Data
public class RolePermission {

    /**
     * 角色id
     */
    private long roleId;

    /**
     * 权限标示
     */
    private String permissionCode;

}
