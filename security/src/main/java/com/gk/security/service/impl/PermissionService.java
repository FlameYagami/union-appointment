package com.gk.security.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.DataStatus;
import com.gk.common.enums.MenuType;
import com.gk.common.enums.SysStatus;
import com.gk.common.enums.YesOrNo;
import com.gk.common.model.dto.LabelResp;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.ServletExtUtils;
import com.gk.common.utils.TreeUtils;
import com.gk.framework.helper.SysRoleHelper;
import com.gk.framework.manager.MenuCacheManager;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.manager.RoleCacheManager;
import com.gk.framework.model.bo.system.sysOpenConfig.OpenConfigCache;
import com.gk.framework.model.dto.system.sysRole.SysRoleLabelResp;
import com.gk.framework.model.entity.system.SysMenu;
import com.gk.framework.model.entity.system.SysRole;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.service.intf.system.IRedisCacheService;
import com.gk.framework.service.intf.system.ISysDeptService;
import com.gk.framework.service.intf.system.ISysUserDeptService;
import com.gk.framework.service.intf.system.ISysUserService;
import com.gk.framework.utils.LoginUserUtils;
import com.gk.security.model.dto.sysPermission.AuthMenuResp;
import com.gk.security.model.dto.sysPermission.AuthPermissionResp;
import com.gk.security.model.dto.sysPermission.DeptChangeReq;
import com.gk.security.model.dto.sysPermission.RoleChangeReq;
import com.gk.security.service.intf.IPermissionService;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限业务实现类
 *
 * @author Flame
 * @date 2023-02-02 11:28
 **/

@Service
@Slf4j
public class PermissionService implements IPermissionService {

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysUserDeptService sysUserDeptService;

    @Resource
    private ISysDeptService sysDeptService;

    @Resource
    private IRedisCacheService redisCacheService;

    /**
     * 切换角色
     */
    @Override
    public void changeRole(RoleChangeReq req) {
        // 参数校验
        long userId = LoginUserUtils.getUserId();
        List<Long> roleIds = SysRoleHelper.getInstance().getEnableRoleIdByUserId(userId);
        if (!roleIds.contains(req.getRoleId())) {
            throw new SysException(SysStatus.DATA_EXCEPTION_AND_REFRESH_PAGE);
        }
        // Redis缓存变更
        RedisCacheManager.getInstance().updateRedisRoleId(req.getRoleId());
        RedisCacheManager.getInstance().updateLastRoleId(userId, req.getRoleId());
    }

    /**
     * 切换部门
     */
    @Override
    public void changeDept(DeptChangeReq req) {
        // 参数校验
        long userId = LoginUserUtils.getUserId();
        List<Long> deptIds = sysUserDeptService.getEnableDeptIds(userId);
        if (!deptIds.contains(req.getDeptId())) {
            throw new SysException(SysStatus.DATA_EXCEPTION_AND_REFRESH_PAGE);
        }
        // Redis缓存变更
        RedisCacheManager.getInstance().updateRedisDeptId(req.getDeptId());
        RedisCacheManager.getInstance().updateLastDeptId(userId, req.getDeptId());
    }

    @Override
    public boolean hasAnyPermissions(String[] permissions) {
        // 如果为空，说明已经有权限
        if (ArrayUtil.isEmpty(permissions)) {
            return true;
        }

        long userId = LoginUserUtils.getUserId();
        // 判断是否是超管
        if (CommonConstant.TOP_ID == userId) {
            return true;
        }

        // 获得当前登录的角色
        long roleId = RedisCacheManager.getInstance().getRedisRoleId();
        List<String> rolePermissions = RoleCacheManager.getInstance().getPermissionCodes(roleId);
        // 角色拥有的权限判空
        if (CollUtil.isEmpty(rolePermissions)) {
            return false;
        }

        // 遍历权限，判断是否有一个满足
        return CollUtil.containsAny(rolePermissions, Sets.newHashSet(permissions));
    }

    @Override
    public boolean hasAnyRoles(String[] roles) {
        // 如果为空，说明已经有权限
        if (ArrayUtil.isEmpty(roles)) {
            return true;
        }

        long userId = LoginUserUtils.getUserId();
        // 判断是否是超管
        if (CommonConstant.TOP_ID == userId) {
            return true;
        }

        // 获得当前登录的角色
        long roleId = RedisCacheManager.getInstance().getRedisRoleId();
        String roleCode = RoleCacheManager.getInstance().findByRoleId(roleId).getRoleCode();

        // 遍历当前用户是否含有需要的角色
        return Arrays.asList(roles).contains(roleCode);
    }

    /**
     * 获取权限信息
     */
    @Override
    public AuthPermissionResp getAuthPermission() {
        long userId = LoginUserUtils.getUserId();

        // 构建用户拥有的部门标签
        List<Long> deptIds = sysUserDeptService.getEnableDeptIds(userId);
        List<LabelResp> deptLabels = sysDeptService.listDeptLabel(deptIds);

        // 构建用户拥有的角色标签
        long roleId = RedisCacheManager.getInstance().getRedisRoleId();
        List<Long> roleIds = SysRoleHelper.getInstance().getEnableRoleIdByUserId(userId);
        List<SysRoleLabelResp> roleLabels = roleIds.stream()
                .map(it -> RoleCacheManager.getInstance().findByRoleId(it))
                .filter(it -> DataStatus.NORMAL.value.equals(it.getDataStatus()))
                .sorted(Comparator.comparing(SysRole::getRoleOrder))
                .map(SysRole::toSysRoleLabelResp)
                .collect(Collectors.toList());
        String roleCode = RoleCacheManager.getInstance().findByRoleId(roleId).getRoleCode();

        // 构建权限
        Set<String> permissions = new HashSet<>();
        // 判断是否是超管
        if (CommonConstant.TOP_ID == userId) {
            permissions.add(CommonConstant.SUPER_ADMIN_PERMISSION);
        } else {
            permissions.addAll(RoleCacheManager.getInstance().getPermissionCodes(roleId));
        }

        // 查询用户
        SysUser sysUser = sysUserService.getById(userId);

        // 构建返回模型
        return new AuthPermissionResp()
                .setPermissions(permissions)
                .setDeptId(RedisCacheManager.getInstance().getRedisDeptId())
                .setDeptLabels(deptLabels)
                .setRoleId(roleId)
                .setRoleCode(roleCode)
                .setRoleLabels(roleLabels)
                .setUserId(sysUser.getId())
                .setNickname(sysUser.getNickname())
                .setUsername(sysUser.getUsername())
                .setPasswordChanged(sysUser.getPasswordChanged());
    }

    /**
     * 获取菜单信息
     */
    @Override
    public List<AuthMenuResp> getAuthMenu() {
        long userId = LoginUserUtils.getUserId();

        List<MenuType> menuTypes = Arrays.asList(MenuType.CATALOG, MenuType.MENU);
        List<SysMenu> sysMenus = new ArrayList<>();

        // 判断是否是超管
        if (CommonConstant.TOP_ID == userId) {
            sysMenus.addAll(MenuCacheManager.getInstance().listMenu(menuTypes));
        } else {
            long roleId = RedisCacheManager.getInstance().getRedisRoleId();
            List<Long> menuIds = new ArrayList<>(RoleCacheManager.getInstance().getMenuIds(roleId));
            sysMenus.addAll(MenuCacheManager.getInstance().listMenu(menuTypes, menuIds));
        }
        // 移除停用的菜单
        sysMenus.removeIf(it -> DataStatus.DISABLE.value.equals(it.getDataStatus()));

        // 构建基本菜单模型
        List<AuthMenuResp> resp = sysMenus.stream()
                .sorted(Comparator.comparing(SysMenu::getMenuOrder))
                .map(this::createAuthMenuResp)
                .collect(Collectors.toList());

        // 构建树形结构
        return TreeUtils.buildTree(resp);
    }

    /**
     * 判断是否有权限(第三方对接权限)
     */
    @Override
    public boolean hasAnyOpenPermission(String[] permissions) {
        // 如果为空，说明已经有权限
        if (ArrayUtil.isEmpty(permissions)) {
            return true;
        }

        // 从请求中获取openId
        HttpServletRequest request = ServletExtUtils.getRequest();
        String openId = request.getHeader(CommonConstant.OPEN_ID_HEADER);
        if (StrUtil.isEmpty(openId)) {
            return false;
        }

        // 通过openId查询第三方配置的缓存信息
        OpenConfigCache cache = redisCacheService.getOpenConfig(openId);
        if (null == cache) {
            return false;
        }

        // 获得第三方对接厂商的角色
        List<String> rolePermissions = RoleCacheManager.getInstance().getPermissionCodes(cache.getRoleId());
        // 角色拥有的权限判空
        if (CollUtil.isEmpty(rolePermissions)) {
            return false;
        }

        // 遍历权限，判断是否有一个满足
        return CollUtil.containsAny(rolePermissions, Sets.newHashSet(permissions));
    }

    /**
     * 创建菜单响应
     */
    private AuthMenuResp createAuthMenuResp(SysMenu sysMenu) {
        // 判断是否为链接地址
        boolean isLinkUrl = HttpUtil.isHttp(sysMenu.getMenuPath()) || HttpUtil.isHttps(sysMenu.getMenuPath());
        return new AuthMenuResp(sysMenu.getId(), sysMenu.getParentId())
                .setName(sysMenu.getMenuName())
                .setPath(sysMenu.getMenuPath())
                .setComponent(sysMenu.getMenuComponent())
                .setIcon(sysMenu.getMenuIcon())
                .setType(sysMenu.getMenuType())
                .setActiveName(sysMenu.getActiveName())
                .setHidden(YesOrNo.NO.value.equals(sysMenu.getMenuShow()))
                .setLinkUrl(isLinkUrl ? sysMenu.getMenuPath() : null)
                .setKeepAlive(YesOrNo.YES.value.equals(sysMenu.getMenuCached()));
    }

}
