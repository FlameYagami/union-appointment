package com.gk.security.service.impl;

import cn.hutool.core.util.StrUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.ExceptionType;
import com.gk.common.enums.OperateStatus;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.IpUtils;
import com.gk.common.utils.ServletExtUtils;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.model.bo.security.LoginUser;
import com.gk.framework.service.intf.system.ISysExceptionLogService;
import com.gk.framework.service.intf.system.ISysLoginLogService;
import com.gk.security.service.intf.IAuthTokenService;
import com.gk.security.service.intf.ISysLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登录业务处理类
 *
 * @author Flame
 * @since 2022-11-30 11:31
 */

@Service
@Slf4j
public class SysLoginService implements ISysLoginService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private IAuthTokenService authTokenService;
    @Resource
    private ISysLoginLogService sysLoginLogService;
    @Resource
    private ISysExceptionLogService exceptionLogService;

    /**
     * 登录密码错误次数限制
     */
    private static final int LOGIN_ERROR_LIMIT = 10;

    /**
     * 登录验证
     */
    @Override
    public String login(String username, String password, String aesKey, String aesIv) {
        String errorCount = RedisCacheManager.getInstance().getLoginErrorCount(username);
        int count = StrUtil.isEmpty(errorCount) ? 0 : Integer.parseInt(errorCount);
        // 超出错误限制次数, 锁定账户, admin账号不会被锁定
        if (count >= LOGIN_ERROR_LIMIT && !CommonConstant.SUPER_ADMIN_USERNAME.equals(username)) {
            throw new SysException(SysStatus.ACCOUNT_LOCKED);
        }

        // 异地ip检测
        String loginIp = IpUtils.getIpAddress(ServletExtUtils.getRequest());
        exceptionLogService.checkAndSaveRemoteLoginLog(username, loginIp);

        // 用户验证
        Authentication authentication;
        try {
            // 该方法会去调用 SysUserDetailsService.loadUserByUsername
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            RedisCacheManager.getInstance().loginError(username, String.valueOf(count + 1));
            // 超出错误限制次数, 记录异常日志
            if (count + 1 >= LOGIN_ERROR_LIMIT && !CommonConstant.SUPER_ADMIN_USERNAME.equals(username)) {
                exceptionLogService.saveBruteForceLoginLog(username, loginIp);
                exceptionLogService.saveExceptLog(username, loginIp, ExceptionType.ACCOUNT_LOCKED);
            }
            sysLoginLogService.saveSysLoginInfo(username, CommonConstant.TOP_ID, SysStatus.ACCOUNT_OR_PASSWORD_ERROR.getMessage(), OperateStatus.FAILED);
            throw new SysException(SysStatus.ACCOUNT_OR_PASSWORD_ERROR);
        } catch (Exception e) {
            sysLoginLogService.saveSysLoginInfo(username, CommonConstant.TOP_ID, SysStatus.FAILED.getMessage(), OperateStatus.FAILED);
            throw new SysException(e.getMessage());
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 获取最后一次账号登入时使用的角色和部门
        long lastRoleId = RedisCacheManager.getInstance().getLastRoleId(loginUser.getUserId());
        long lastDeptId = RedisCacheManager.getInstance().getLastDeptId(loginUser.getUserId());

        // 生成token
        String token = authTokenService.createToken(loginUser.getUserId(), lastRoleId, lastDeptId, aesKey, aesIv);
        // 保存登录信息
        if (StrUtil.isNotEmpty(token)) {
            sysLoginLogService.saveSysLoginInfo(loginUser.getUsername(), lastDeptId, SysStatus.LOGIN_SUCCESS.getMessage(), OperateStatus.SUCCESS);
        }

        return token;
    }

}
