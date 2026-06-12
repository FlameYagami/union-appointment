package com.gk.framework.manager;

import cn.hutool.extra.spring.SpringUtil;
import com.gk.common.enums.EnabledType;
import com.gk.framework.model.bo.role.RoleMenu;
import com.gk.framework.model.bo.role.RolePermission;
import com.gk.framework.model.entity.system.SysRole;
import com.gk.framework.service.intf.system.ISysRoleService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色缓存管理类
 *
 * @author Flame
 * @date 2023-04-13 9:34
 **/
@Slf4j
public class RoleCacheManager {

    private final ISysRoleService sysRoleService;

    public RoleCacheManager() {
        this.sysRoleService = SpringUtil.getBean(ISysRoleService.class);
    }

    /**
     * 懒加载模式
     */
    public static RoleCacheManager getInstance() {
        return RoleCacheManager.Holder.INSTANCE;
    }

    private static class Holder {
        public static final RoleCacheManager INSTANCE = new RoleCacheManager();
    }

    /**
     * 角色缓存
     */
    private final Map<Long, SysRole> roleCache = new HashMap<>();

    /**
     * 角色权限缓存
     */
    private final Map<Long, Set<String>> rolePermissionCache = new HashMap<>();

    /**
     * 角色菜单缓存
     */
    private final Map<Long, Set<Long>> roleMenuCache = new HashMap<>();

    /**
     * 初始化角色缓存
     */
    public void refreshCache() {
        refreshRoleCache();
        refreshRolePermissionCache();
        refreshRoleMenuCache();
    }

    /**
     * 初始化角色缓存(包含停用)
     */
    public void refreshRoleCache() {
        roleCache.clear();
        roleCache.putAll(
                sysRoleService.lambdaQuery()
                        .eq(SysRole::getEnabled, EnabledType.ENABLE.value)
                        .list().stream()
                        .collect(Collectors.toMap(SysRole::getId, it -> it))
        );
        log.info("Init roleCache size({})", roleCache.size());
    }

    /**
     * 初始化角色权限缓存(包含停用)
     */
    public void refreshRolePermissionCache() {
        rolePermissionCache.clear();
        rolePermissionCache.putAll(
                sysRoleService.listRolePermission().stream()
                        .collect(Collectors.groupingBy(RolePermission::getRoleId, Collectors.mapping(RolePermission::getPermissionCode, Collectors.toSet())))
        );
        log.info("Init rolePermissionCache size({})", rolePermissionCache.size());
    }

    /**
     * 初始化角色菜单缓存(包含停用)
     */
    public void refreshRoleMenuCache() {
        roleMenuCache.clear();
        roleMenuCache.putAll(
                sysRoleService.listRoleMenu().stream()
                        .collect(Collectors.groupingBy(RoleMenu::getRoleId, Collectors.mapping(RoleMenu::getMenuId, Collectors.toSet())))
        );
        log.info("Init roleMenuCache size({})", roleMenuCache.size());
    }

    /**
     * 从缓存中获取角色权限
     */
    public List<String> getPermissionCodes(long roleId) {
        return new ArrayList<>(rolePermissionCache.getOrDefault(roleId, new HashSet<>()));
    }

    /**
     * 从缓存中获取角色菜单
     */
    public List<Long> getMenuIds(long roleId) {
        return new ArrayList<>(roleMenuCache.getOrDefault(roleId, new HashSet<>()));
    }

    /**
     * 从缓存中获取角色
     */
    public SysRole findByRoleId(long roleId) {
        return roleCache.get(roleId);
    }

    /**
     * 从缓存中获取角色
     */
    public SysRole findByRoleCode(String roleCode) {
        return roleCache.values().stream()
                .filter(it -> it.getRoleCode().equals(roleCode))
                .collect(Collectors.toList())
                .get(0);
    }

    /**
     * 从缓存中获取角色编码
     *
     * @param roleId 角色id
     */
    public String findRoleCode(long roleId) {
        SysRole sysRole = findByRoleId(roleId);
        if (sysRole == null) {
            return null;
        }
        return sysRole.getRoleCode();
    }
}
