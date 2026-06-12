package com.gk.framework.helper;

import cn.hutool.extra.spring.SpringUtil;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.service.intf.system.ISysUserService;

/**
 * 注释
 *
 * @author Flame
 * @since 2023-06-12 11:30
 **/
public class SysUserHelper {

    private final ISysUserService sysUserService;

    public SysUserHelper() {
        sysUserService = SpringUtil.getBean(ISysUserService.class);
    }

    public static SysUserHelper getInstance() {
        return SysUserHelper.Holder.INSTANCE;
    }

    private static class Holder {
        public static final SysUserHelper INSTANCE = new SysUserHelper();
    }

    public SysUser findByUserId(long userId) {
        return sysUserService.getById(userId);
    }


}
