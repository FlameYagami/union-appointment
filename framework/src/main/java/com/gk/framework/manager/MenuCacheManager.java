package com.gk.framework.manager;

import cn.hutool.extra.spring.SpringUtil;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.MenuType;
import com.gk.common.enums.YesOrNo;
import com.gk.framework.model.entity.system.SysMenu;
import com.gk.framework.service.intf.system.ISysMenuService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单缓存管理类
 *
 * @author Flame
 * @date 2023-04-13 9:16
 **/
@Slf4j
public class MenuCacheManager {

    private final ISysMenuService sysMenuService;

    public MenuCacheManager() {
        this.sysMenuService = SpringUtil.getBean(ISysMenuService.class);
    }

    /**
     * 懒加载模式
     */
    public static MenuCacheManager getInstance() {
        return MenuCacheManager.Holder.INSTANCE;
    }

    private static class Holder {
        public static final MenuCacheManager INSTANCE = new MenuCacheManager();
    }

    /**
     * 菜单缓存
     */
    private final Map<Long, SysMenu> menuCache = new HashMap<>();

    /**
     * 初始化菜单缓存
     */
    public void refreshCache() {
        menuCache.clear();
        menuCache.putAll(
                sysMenuService.lambdaQuery()
                        .eq(SysMenu::getEnabled, EnabledType.ENABLE.value)
                        .list().stream()
                        .collect(Collectors.toMap(SysMenu::getId, it -> it))
        );
        log.info("Init menuCache size({})", menuCache.size());
    }

    /**
     * 从缓存中获取指定类型的菜单
     */
    public List<SysMenu> listMenu(List<MenuType> menuTypes) {
        if (menuTypes.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> menuTypeValues = MenuType.getValues(menuTypes);
        return menuCache.values().stream()
                .filter(it -> menuTypeValues.contains(it.getMenuType()))
                .collect(Collectors.toList());
    }

    /**
     * 从缓存中获取指定类型的菜单
     */
    public List<SysMenu> listMenu(List<MenuType> menuTypes, List<Long> menuIds) {
        if (menuTypes.isEmpty() || menuIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> menuTypeValues = MenuType.getValues(menuTypes);
        return menuCache.values().stream()
                .filter(it -> menuTypeValues.contains(it.getMenuType()) && YesOrNo.YES.value.equals(it.getMenuOpen()) && menuIds.contains(it.getId()))
                .collect(Collectors.toList());
    }


}
