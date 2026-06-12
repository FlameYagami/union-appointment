package com.gk.framework.manager;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 缓存初始化管理器
 *
 * @author Flame
 * @date 2023-04-13 10:34
 **/
@Component
public class CacheInitManager {

    @PostConstruct
    public void lateInit() {
        // 菜单缓存初始化
        MenuCacheManager.getInstance().refreshCache();
        // 角色缓存初始化
        RoleCacheManager.getInstance().refreshCache();
    }

}
