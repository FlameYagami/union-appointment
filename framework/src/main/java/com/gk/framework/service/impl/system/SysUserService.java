package com.gk.framework.service.impl.system;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.*;
import com.gk.common.model.dto.LabelResp;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.IpUtils;
import com.gk.common.utils.ServletExtUtils;
import com.gk.framework.constant.RedisConstant;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.mapper.system.SysUserMapper;
import com.gk.framework.model.bo.system.sysUser.SysUserExt;
import com.gk.framework.model.dto.system.sysUser.PasswordAlterReq;
import com.gk.framework.model.dto.system.sysUser.PasswordChangeReq;
import com.gk.framework.model.dto.system.sysUser.UserStatusChangeReq;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.service.intf.system.ISysExceptionLogService;
import com.gk.framework.service.intf.system.ISysRoleService;
import com.gk.framework.service.intf.system.ISysUserService;
import com.gk.framework.utils.LoginUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

/**
 * 用户表 服务实现类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */

@Service
@Slf4j
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysExceptionLogService exceptionLogService;

    /**
     * 单个查询
     */
    @Override
    public SysUser findByUserId(long userId) {
        return lambdaQuery()
                .eq(SysUser::getId, userId)
                .eq(SysUser::getDataStatus, DataStatus.NORMAL.value)
                .eq(SysUser::getEnabled, EnabledType.ENABLE.value)
                .one();
    }

    /**
     * 通过账号查询用户信息
     */
    @Override
    public SysUser findByUsername(String username) {
        return lambdaQuery()
                .eq(SysUser::getEnabled, EnabledType.ENABLE.value)
                .eq(SysUser::getDataStatus, DataStatus.NORMAL.value)
                .eq(SysUser::getUsername, username)
                .one();
    }

    /**
     * 修改用户状态
     */
    @Override
    public void changeUserStatus(UserStatusChangeReq req) {
        boolean result = lambdaUpdate()
                .set(SysUser::getDataStatus, req.getDataStatus())
                .eq(SysUser::getId, req.getId())
                .update();
        if (!result) {
            log.error("Change UserStatus Error: id({})", req.getId());
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 查询用户扩展信息(包含所属部门、拥有角色)
     */
    @Override
    public SysUserExt findSysUserExt(long userId) {
        return baseMapper.findSysUserExt(userId);
    }

    /**
     * 用户密码重置
     */
    @CacheEvict(value = RedisConstant.USER_INFO_KEY, key = "#id")
    @Override
    public String resetPassword(long id) {
        // 生成随机的密码返回
        String position1 = RandomUtil.randomString(RandomUtil.BASE_CHAR.toUpperCase(Locale.ROOT), 1);
        String position2 = RandomUtil.randomString(RandomUtil.BASE_CHAR, 3);
        String position3 = RandomUtil.randomString("@#$%&*_+-=?!", 1);
        String position4 = RandomUtil.randomNumbers(4);
        String password = position1 + position2 + position3 + position4;
        // 保存修改
        boolean result = lambdaUpdate()
                .set(SysUser::getPassword, SysUser.encryptPassword(password))
                .set(SysUser::getPasswordChanged, YesOrNo.NO.value) // 密码重置字段恢复0
                .eq(SysUser::getId, id)
                .update();
        if (!result) {
            log.error("Reset Password Error: userId({})", id);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        // 将重置的密码返回给前端
        return password;
    }

    /**
     * 用户密码变更
     */
    @CacheEvict(value = RedisConstant.USER_INFO_KEY, key = "#req.id")
    @Override
    public void changePassword(PasswordChangeReq req) {
        // 原密码校验
        SysUser dbEntity = getById(req.getId());
        if (!new BCryptPasswordEncoder().matches(req.getOldPassword(), dbEntity.getPassword())) {
            throw new SysException(SysStatus.OLD_PASSWORD_ERROR);
        }

        // 密码包含用户名校验
        if (req.getNewPassword().contains(dbEntity.getUsername())) {
            throw new SysException(SysStatus.NEW_PASSWORD_CAN_NOT_CONTAIN_USERNAME);
        }

        // 原密码与新密码校验
        if (req.getOldPassword().equals(req.getNewPassword())) {
            throw new SysException(SysStatus.PASSWORD_SAME);
        }

        // 保存密码变更日志信息
        exceptionLogService.saveExceptLog(dbEntity.getUsername(), IpUtils.getIpAddress(ServletExtUtils.getRequest()), ExceptionType.PASSWORD_CHANGED);

        // 保存修改
        boolean result = lambdaUpdate()
                .set(SysUser::getPassword, SysUser.encryptPassword(req.getNewPassword()))
                .set(SysUser::getPasswordChanged, YesOrNo.YES.value) // 密码重置字段强制修改为1
                .eq(SysUser::getId, req.getId())
                .update();
        if (!result) {
            log.error("Change Password Error: userId({})", req.getId());
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

    }

    /**
     * 用户密码变更(重置密码后需要变更)
     */
    @Override
    public void alterPassword(PasswordAlterReq req) {
        long userId = LoginUserUtils.getUserId();
        // 校验密码是否已经变更
        boolean passwordChanged = lambdaQuery()
                .eq(SysUser::getId, userId)
                .eq(SysUser::getPasswordChanged, YesOrNo.YES.value)
                .exists();
        if (passwordChanged) {
            throw new SysException(SysStatus.PASSWORD_HAS_CHANGED);
        }

        SysUser dbEntity = getById(userId);

        // 密码包含用户名校验
        if (req.getNewPassword().contains(dbEntity.getUsername())) {
            throw new SysException(SysStatus.NEW_PASSWORD_CAN_NOT_CONTAIN_USERNAME);
        }

        // 原密码与新密码校验
        if (new BCryptPasswordEncoder().matches(req.getNewPassword(), dbEntity.getPassword())) {
            throw new SysException(SysStatus.PASSWORD_SAME);
        }

        // 保存修改
        String encryptPassword = SysUser.encryptPassword(req.getNewPassword());
        boolean result = lambdaUpdate()
                .set(SysUser::getPassword, encryptPassword)
                .set(SysUser::getPasswordChanged, YesOrNo.YES.value) // 密码重置字段强制修改为1
                .eq(SysUser::getId, userId)
                .update();
        if (!result) {
            log.error("Alter Password Error: userId({})", userId);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // Redis更新用户密码
        RedisCacheManager.getInstance().alterPassword(userId, encryptPassword);
    }

    /**
     * 角色标签查询(用于用户设置中的可选角色勾选)
     */
    @Override
    public List<LabelResp> listMgmtRoleLabel() {
        return sysRoleService.listMgmtRoleLabel();
    }

}

