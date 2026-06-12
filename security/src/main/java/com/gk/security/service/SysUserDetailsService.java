package com.gk.security.service;

import com.gk.common.enums.DataStatus;
import com.gk.common.enums.SysStatus;
import com.gk.common.enums.YesOrNo;
import com.gk.common.model.exception.SysException;
import com.gk.framework.helper.SysRoleHelper;
import com.gk.framework.model.bo.security.LoginUser;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.service.intf.system.ISysUserDeptService;
import com.gk.framework.service.intf.system.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Flame
 * @since 2022-11-30 11:39
 */

@Service
@Slf4j
public class SysUserDetailsService implements UserDetailsService {

    @Resource
    private ISysUserService iSysUserService;

    @Resource
    private ISysUserDeptService sysUserDeptService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = iSysUserService.findByUsername(username);
        if (null == sysUser) {
            throw new SysException(SysStatus.ACCOUNT_OR_PASSWORD_ERROR);
        }

        // 停用判定
        if (DataStatus.DISABLE.value.equals(sysUser.getDataStatus())) {
            log.error("Auth User Error: user dataStatus is disable, username: {}", username);
            throw new SysException(SysStatus.ACCOUNT_DISABLE);
        }

        // 黑名单判定
        if (YesOrNo.YES.value.equals(sysUser.getBlacklistStatus())) {
            log.error("Auth User Error: account in blacklist, username: {}", username);
            throw new SysException(SysStatus.ACCOUNT_IN_BLACKLIST);
        }

        // 部门停用判定
        List<Long> enableDeptIds = sysUserDeptService.getEnableDeptIds(sysUser.getId());
        if (enableDeptIds.isEmpty()) {
            throw new SysException(SysStatus.ACCOUNT_HAS_NO_DEPT);
        }

        // 角色停用判定
        List<Long> enableRoleIds = SysRoleHelper.getInstance().getEnableRoleIdByUserId(sysUser.getId());
        if (enableRoleIds.isEmpty()) {
            throw new SysException(SysStatus.ACCOUNT_HAS_NO_ROLE);
        }

        return createLoginUser(sysUser);
    }

    public UserDetails createLoginUser(SysUser sysUser) {
        return new LoginUser()
                .setSysUser(sysUser);
    }

}
