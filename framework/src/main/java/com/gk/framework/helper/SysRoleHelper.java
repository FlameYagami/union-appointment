package com.gk.framework.helper;

import cn.hutool.extra.spring.SpringUtil;
import com.gk.common.enums.DataStatus;
import com.gk.framework.manager.RoleCacheManager;
import com.gk.framework.model.entity.system.SysRole;
import com.gk.framework.service.intf.system.ISysUserRoleService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 注释
 *
 * @author Flame
 * @since 2023-06-12 11:30
 **/
public class SysRoleHelper {

    private final ISysUserRoleService sysUserRoleService;

    public SysRoleHelper() {
        sysUserRoleService = SpringUtil.getBean(ISysUserRoleService.class);
    }

    public static SysRoleHelper getInstance() {
        return SysRoleHelper.Holder.INSTANCE;
    }

    private static class Holder {
        public static final SysRoleHelper INSTANCE = new SysRoleHelper();
    }

    /**
     * 通过用户id查询用户拥有的角色id
     *
     * @param userId 用户id
     * @return 角色id
     */
    public List<Long> getEnableRoleIdByUserId(long userId) {
        return sysUserRoleService.getEnableRoleIds(userId);
    }

    /**
     * 通过用户id查询用户拥有的角色名称
     *
     * @param userId 用户id
     * @return 角色名称
     */
    public List<String> getEnableRoleNameByUserId(long userId) {
        return getEnableRoleIdByUserId(userId).stream()
                .map(it -> RoleCacheManager.getInstance().findByRoleId(it))
                .filter(it -> DataStatus.NORMAL.value.equals(it.getDataStatus()))
                .map(SysRole::getRoleName)
                .collect(Collectors.toList());
    }

}
