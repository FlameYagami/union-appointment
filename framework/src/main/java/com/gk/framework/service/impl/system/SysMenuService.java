package com.gk.framework.service.impl.system;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.*;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.JsonUtils;
import com.gk.common.utils.TreeUtils;
import com.gk.framework.manager.MenuCacheManager;
import com.gk.framework.manager.RoleCacheManager;
import com.gk.framework.mapper.system.SysMenuMapper;
import com.gk.framework.model.dto.system.sysMenu.*;
import com.gk.framework.model.entity.system.SysMenu;
import com.gk.framework.service.intf.system.ISysMenuService;
import com.gk.framework.service.intf.system.ISysRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表 服务实现类
 *
 * @author Flame
 * @since 2022-12-29 09:30:38
 */

@Service
@Slf4j
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Resource
    private ISysRoleMenuService sysRoleMenuService;

    /**
     * 树形结构查询
     */
    @Override
    public List<SysMenuTreeResp> listTree(SysMenuTreeReq req) {
        List<SysMenuTreeResp> resp = baseMapper.listSysMenuTree(req);
        return TreeUtils.buildTree(resp);
    }

    /**
     * 新增
     */
    @Override
    public void add(SysMenuAddReq req) {
        // 校验父级菜单id合法性(如果为顶级目录, 只包含id, menuOpen, menuType字段)
        SysMenu parentMenu = checkParentIdLegal(null, req.getParentId());
        // 校验菜单类型
        checkMenuType(parentMenu, req.getMenuType());
        // 校验内外链合法性
        if (!MenuType.BUTTON.value.equals(req.getMenuType())) {
            SysMenuBaseReq baseReq = req.getSysMenuBases().get(0);
            checkInnerLink(baseReq.getInnerLink(), baseReq.getMenuPath());
        }

        List<SysMenu> sysMenus = req.toEntities(parentMenu.getMenuOpen());
        if (!saveBatch(sysMenus)) {
            log.error("Add SysMenu Error: {}", JsonUtils.toJson(sysMenus));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        // 刷新菜单缓存
        MenuCacheManager.getInstance().refreshCache();
    }

    /**
     * 修改
     */
    @Transactional
    @Override
    public void edit(SysMenuEditReq req) {
        // 校验父级菜单id合法性(如果为顶级目录, 只包含id和menuOpen字段)
        SysMenu parentMenu = checkParentIdLegal(req.getId(), req.getParentId());
        // 校验菜单类型
        checkMenuType(parentMenu, req.getMenuType());
        // 校验内外链合法性
        checkInnerLink(req.getInnerLink(), req.getMenuPath());

        // 批量修改子菜单
        SysMenu dbEntity = getById(req.getId());
        if (!updateChild(dbEntity, req)) {
            log.error("Edit SysMenu Error in update child menu: {}", JsonUtils.toJson(req));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 修改当前菜单
        SysMenu sysMenu = req.toEntity(parentMenu.getMenuOpen());
        if (!updateById(sysMenu)) {
            log.error("Edit SysMenu Error: {}", JsonUtils.toJson(sysMenu));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        // 刷新菜单缓存
        MenuCacheManager.getInstance().refreshCache();
        // 刷新角色权限与角色菜单缓存
        ThreadUtil.execAsync(() -> {
            RoleCacheManager.getInstance().refreshRoleMenuCache();
            RoleCacheManager.getInstance().refreshRolePermissionCache();
        });
    }

    private boolean updateChild(SysMenu dbEntity, SysMenuEditReq req) {
        // 查询所有下级菜单
        List<SysMenu> childMenus = listChildMenu(req.getId(), false);
        if (childMenus.isEmpty()) {
            return true;
        }

        // 数据库中与修改的数据状态一致则不需要修改
        String dataStatus = null;
        if (!dbEntity.getDataStatus().equals(req.getDataStatus())) {
            dataStatus = req.getDataStatus();
        }
        String finalDataStatus = dataStatus;

        // 数据库中菜单开放给全部用户改为超管的情况下才修改
        String menuOpen = null;
        if (YesOrNo.YES.value.equals(dbEntity.getMenuOpen()) && YesOrNo.NO.value.equals(req.getMenuOpen())) {
            menuOpen = YesOrNo.NO.value;
        }
        String finalMenuOpen = menuOpen;

        // 调整需要修改的
        childMenus.forEach(menu -> {
            if (null != finalDataStatus) {
                menu.setDataStatus(finalDataStatus); // 自适应修改当前菜单的所有下级菜单数据状态
            }
            if (null != finalMenuOpen) {
                menu.setMenuOpen(finalMenuOpen); // 自适应修改当前菜单的所有下级菜单菜单开放
            }
        });

        // 保存数据
        return updateBatchById(childMenus);
    }

    /**
     * 删除
     */
    @Override
    public void delete(long id) {
        // 校验下级菜单是否存在
        boolean existChild = lambdaQuery()
                .eq(SysMenu::getEnabled, EnabledType.ENABLE.value)
                .eq(SysMenu::getParentId, id)
                .exists();
        if (existChild) {
            throw new SysException(SysStatus.MENU_HAS_CHILD);
        }

        // 删除角色拥有的菜单
        sysRoleMenuService.deleteByMenuId(id);

        // 菜单删除
        boolean result = lambdaUpdate()
                .eq(SysMenu::getId, id)
                .set(SysMenu::getEnabled, EnabledType.DISABLE.value)
                .update();
        if (!result) {
            log.error("Delete SysMenu Error: {}", id);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        ThreadUtil.execAsync(() -> {
            // 刷新菜单缓存
            MenuCacheManager.getInstance().refreshCache();
            // 刷新角色权限与角色菜单缓存
            RoleCacheManager.getInstance().refreshRoleMenuCache();
            RoleCacheManager.getInstance().refreshRolePermissionCache();
        });
    }

    /**
     * 单个查询
     */
    @Override
    public SysMenuResp findOne(long id) {
        SysMenu sysMenu = lambdaQuery()
                .eq(SysMenu::getId, id)
                .eq(SysMenu::getEnabled, EnabledType.ENABLE.value)
                .one();
        if (sysMenu == null) {
            return null;
        }
        return sysMenu.toResp();
    }

    /**
     * 获取菜单树形结构列表
     */
    @Override
    public List<TreeLabelResp> listTreeLabel() {
        List<MenuType> menuTypes = Arrays.asList(MenuType.CATALOG, MenuType.MENU, MenuType.BUTTON);
        List<TreeLabelResp> resp = MenuCacheManager.getInstance().listMenu(menuTypes).stream()
                .sorted(Comparator.comparing(SysMenu::getMenuOrder).thenComparing(SysMenu::getId)) // 菜单排序
                .map(SysMenu::toTreeLabelResp)
                .collect(Collectors.toList());
        return TreeUtils.buildTree(resp);
    }

    /**
     * 校验父级菜单id合法性(新增或是修改)
     */
    private SysMenu checkParentIdLegal(Long id, long parentId) {
        // 顶级菜单直接放行
        if (CommonConstant.TOP_VIRTUAL_ID == parentId) {
            return new SysMenu()
                    .setId(CommonConstant.TOP_VIRTUAL_ID)
                    .setMenuOpen(YesOrNo.YES.value)
                    .setMenuType(MenuType.CATALOG.getValue());
        }
        // 父级菜单不存在
        SysMenu sysMenu = getById(parentId);
        if (null == sysMenu) {
            throw new SysException(SysStatus.MENU_PARENT_NOT_EXITS);
        }
        // 父级菜单被删除或是停用
        if (EnabledType.DISABLE.value.equals(sysMenu.getEnabled()) || DataStatus.DISABLE.value.equals(sysMenu.getDataStatus())) {
            throw new SysException(SysStatus.MENU_NOT_ENABLE);
        }

        // 如果是新增则无需进行下面的判定
        if (null == id) {
            return sysMenu;
        }
        // 父级菜单不能自己或是原来的下级菜单
        List<Long> childIds = getEnableChildMenuIds(id, true);
        if (childIds.contains(parentId)) {
            throw new SysException(SysStatus.MENU_PARENT_IS_CHILD);
        }
        return sysMenu;
    }

    /**
     * 校验菜单类型
     */
    private void checkMenuType(SysMenu parentMenu, String menuType) {
        // 父级菜单是顶级菜单直接放行
        if (CommonConstant.TOP_VIRTUAL_ID == parentMenu.getId()) {
            return;
        }

        // 对菜单类型进行判定(菜单的父级菜单必须是目录类型)
        if (MenuType.MENU.value.equals(menuType) && !MenuType.CATALOG.value.equals(parentMenu.getMenuType())) {
            throw new SysException(SysStatus.MENU_PARENT_TYPE_ILLEGAL);
        }
    }

    /**
     * 校验内外链合法性
     */
    private void checkInnerLink(String innerLink, String menuPath) {
        // 内联直接放行
        if (YesOrNo.YES.value.equals(innerLink)) {
            return;
        }
        // 外链校验http或https
        if (HttpUtil.isHttp(menuPath) || HttpUtil.isHttps(menuPath)) {
            return;
        }
        throw new SysException(SysStatus.MENU_OUT_LINK_ILLEGAL);
    }

    /**
     * 获取生效中的父级菜单的子菜单id集合
     */
    public List<Long> getEnableChildMenuIds(long menuId, boolean includeMenuId) {
        List<Long> ids = baseMapper.getEnableChildMenuIds(menuId);
        if (includeMenuId) {
            ids.add(0, menuId);
        }
        return ids;
    }

    /**
     * 获取父级菜单的子菜单id集合
     */
    public List<Long> getChildMenuIds(long menuId, boolean includeMenuId) {
        List<Long> ids = baseMapper.getChildMenuIds(menuId);
        if (includeMenuId) {
            ids.add(0, menuId);
        }
        return ids;
    }

    /**
     * 获取菜单的所有下级菜单
     */
    private List<SysMenu> listChildMenu(long menuId, boolean includeMenuId) {
        List<SysMenu> sysMenus = baseMapper.listChildMenu(menuId);
        if (!includeMenuId) {
            sysMenus.removeIf(it -> it.getId() == menuId);
        }
        return sysMenus;
    }


}

