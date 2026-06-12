package com.gk.framework.service.impl.system;

import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.*;
import com.gk.common.model.dto.LabelResp;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.JsonUtils;
import com.gk.common.utils.TreeUtils;
import com.gk.framework.manager.MenuCacheManager;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.manager.RoleCacheManager;
import com.gk.framework.mapper.system.SysRoleMapper;
import com.gk.framework.model.bo.role.RoleMenu;
import com.gk.framework.model.bo.role.RolePermission;
import com.gk.framework.model.dto.system.base.DataStatusChangeReq;
import com.gk.framework.model.dto.system.sysRole.*;
import com.gk.framework.model.entity.system.SysMenu;
import com.gk.framework.model.entity.system.SysRole;
import com.gk.framework.model.entity.system.SysRoleMenu;
import com.gk.framework.service.intf.system.ISysRoleMenuService;
import com.gk.framework.service.intf.system.ISysRoleService;
import com.gk.framework.service.intf.system.ISysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色表 服务实现类
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */

@Service
@Slf4j
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private ISysRoleMenuService sysRoleMenuService;

    @Resource
    private ISysUserRoleService sysUserRoleService;

    /**
     * 分页查询
     */
    @Override
    public IPage<SysRolePageResp> pageList(SysRolePageReq req) {
        return baseMapper.pageSysRole(req.createPage(), req);
    }

    /**
     * 新增
     */
    @Transactional
    @Override
    public long add(SysRoleAddReq req) {
        // 校验部门编码合法性
        checkRoleCodeLegal(null, req.getRoleCode());

        // 保存角色信息
        SysRole sysRole = req.toEntity();
        if (!save(sysRole)) {
            log.error("Add SysRole Error: {}", JsonUtils.toJson(sysRole));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存角色菜单信息
        List<SysRoleMenu> sysRoleMenus = req.toSysRoleMenus(sysRole.getId());
        if (!sysRoleMenuService.saveBatch(sysRoleMenus)) {
            log.error("Add SysRoleMenu Error: {}", JsonUtils.toJson(sysRole));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 刷新缓存
        ThreadUtil.execAsync(() -> RoleCacheManager.getInstance().refreshCache());
        return sysRole.getId();
    }

    /**
     * 修改
     */
    @Transactional
    @Override
    public void edit(SysRoleEditReq req) {
        // 校验角色是否合理
        checkEntity(req);
        // 校验角色编码合法性
        checkRoleCodeLegal(req.getId(), req.getRoleCode());

        // 保存角色信息
        SysRole sysRole = req.toEntity();
        if (!updateById(sysRole)) {
            log.error("Edit SysRole Error: {}", JsonUtils.toJson(sysRole));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存角色菜单信息
        List<SysRoleMenu> sysRoleMenus = req.toSysRoleMenus(sysRole.getId());
        if (!sysRoleMenuService.modify(req.getId(), sysRoleMenus)) {
            log.error("Update SysRoleMenu Error: {}", JsonUtils.toJson(sysRole));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 刷新缓存
        ThreadUtil.execAsync(() -> RoleCacheManager.getInstance().refreshCache());
    }

    /**
     * 删除
     */
    @Override
    public void delete(long id) {
        // 判断是否为系统数据
        SysRole dbEntity = getById(id);
        if (YesOrNo.YES.value.equals(dbEntity.getSystemData())) {
            throw new SysException(SysStatus.SYSTEM_DATA_CAN_NOT_DELETE);
        }

        // 角色使用校验
        if (sysUserRoleService.hasUseRole(id)) {
            throw new SysException(SysStatus.ROLE_HAS_USER_BY_USER_CAN_NOT_OPERATE);
        }

        // 删除角色
        boolean result = lambdaUpdate()
                .eq(SysRole::getId, id)
                .set(SysRole::getEnabled, EnabledType.DISABLE.value)
                .update();
        if (!result) {
            log.error("Delete SysRole Error: {}", id);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 刷新缓存
        ThreadUtil.execAsync(() -> RoleCacheManager.getInstance().refreshCache());
    }

    /**
     * 单个查询
     */
    @Override
    public SysRoleResp findOne(long id) {
        // 从缓存中获取角色菜单
        List<Long> menuIds = RoleCacheManager.getInstance().getMenuIds(id);
        SysRole sysRole = lambdaQuery()
                .eq(SysRole::getId, id)
                .eq(SysRole::getEnabled, EnabledType.ENABLE.value)
                .one();
        if (sysRole == null) {
            return null;
        }
        return sysRole.toResp().setMenuIds(menuIds);
    }

    /**
     * 角色标签查询(用于角色设置中的可选角色勾选)
     */
    @Override
    public List<LabelResp> listMgmtRoleLabel() {
        long roleId = RedisCacheManager.getInstance().getRedisRoleId();
        SysRole sysRole = getById(roleId);
        return lambdaQuery()
                .select(SysRole::getId, SysRole::getRoleName, SysRole::getDataStatus)
                .eq(SysRole::getEnabled, EnabledType.ENABLE.value)
                .ge(SysRole::getRoleLevel, sysRole.getRoleLevel())
                .ne(SysRole::getRoleCode, SysRoleCode.SUPER_ADMIN.value) // 过滤超管的角色
                .ne(SysRole::getRoleCode, SysRoleCode.SYSTEM_ADMIN.value) // 过滤系统管理员角色
                .list().stream()
                .map(SysRole::toLabelResp)
                .collect(Collectors.toList());
    }

    /**
     * 菜单树形结构查询(用于角色设置中的可选菜单勾选)
     */
    @Override
    public List<TreeLabelResp> listMgmtMenuTreeLabel() {
        List<MenuType> menuTypes = Arrays.asList(MenuType.CATALOG, MenuType.MENU, MenuType.BUTTON);
        List<TreeLabelResp> resp = MenuCacheManager.getInstance().listMenu(menuTypes).stream()
                .filter(it -> YesOrNo.YES.value.equals(it.getMenuOpen()))
                .sorted(Comparator.comparing(SysMenu::getMenuOrder).thenComparing(SysMenu::getId)) // 对菜单进行排序
                .map(SysMenu::toTreeLabelResp)
                .collect(Collectors.toList());
        return TreeUtils.buildTree(resp);
    }

    /**
     * 修改角色状态
     */
    @Override
    public void changeDataStatus(DataStatusChangeReq req) {
        boolean result = lambdaUpdate()
                .set(SysRole::getDataStatus, req.getDataStatus())
                .eq(SysRole::getId, req.getId())
                .update();
        if (!result) {
            log.error("Change DataStatus Error in SysRole: id({})", req.getId());
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 获取所有角色权限(缓存专用)
     */
    @Override
    public List<RolePermission> listRolePermission() {
        return baseMapper.listRolePermission();
    }

    /**
     * 获取所有角色菜单(缓存专用)
     */
    @Override
    public List<RoleMenu> listRoleMenu() {
        return baseMapper.listRoleMenu();
    }

    /**
     * 校验角色编码合法性(新增或是修改)
     */
    private void checkRoleCodeLegal(Long id, String roleCode) {
        boolean exist = lambdaQuery()
                .eq(SysRole::getEnabled, EnabledType.ENABLE.value)
                .eq(SysRole::getRoleCode, roleCode)
                .ne(null != id, SysRole::getId, id)
                .exists();
        if (exist) {
            throw new SysException(SysStatus.ROLE_CODE_EXITS);
        }
    }

    /**
     * 校验角色是否合理
     *
     * @param req 修改的配置
     */
    private void checkEntity(SysRoleEditReq req) {
        SysRole dbEntity = getById(req.getId());
        if (YesOrNo.NO.value.equals(dbEntity.getSystemData())) {
            return;
        }
        // 校验角色编码
        if (!dbEntity.getRoleCode().equals(req.getRoleCode())) {
            throw new SysException(SysStatus.SYSTEM_ROLE_CODE_CAN_NOT_EDIT);
        }
        // 校验角色等级
        if (!dbEntity.getRoleLevel().equals(req.getRoleLevel())) {
            throw new SysException(SysStatus.SYSTEM_ROLE_LEVEL_CAN_NOT_EDIT);
        }
    }
}

