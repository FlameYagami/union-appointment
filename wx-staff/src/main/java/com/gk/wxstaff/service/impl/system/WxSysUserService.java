package com.gk.wxstaff.service.impl.system;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.service.intf.system.ISysUserRoleService;
import com.gk.framework.service.intf.system.ISysUserService;
import com.gk.server.enums.RoleCode;
import com.gk.wxstaff.mapper.system.WxSysUserMapper;
import com.gk.wxstaff.service.intf.system.IWxSysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户表 服务实现类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */

@Service
@Slf4j
public class WxSysUserService extends ServiceImpl<WxSysUserMapper, SysUser> implements IWxSysUserService {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysUserRoleService sysUserRoleService;


}

