package com.gk.security.handler;

import com.gk.common.enums.OperateStatus;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.others.BaseResult;
import com.gk.common.utils.ServletExtUtils;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.model.bo.security.LoginUser;
import com.gk.framework.service.intf.system.ISysLoginLogService;
import com.gk.security.service.intf.IAuthTokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义退出处理类 返回成功
 *
 * @author Flame
 * @since 2022-11-20 10:16
 */

@Configuration
public class LogoutHandler implements LogoutSuccessHandler {

    @Resource
    private IAuthTokenService authTokenService;

    @Resource
    private ISysLoginLogService sysLoginLogService;

    /**
     * 退出处理
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser loginUser = authTokenService.findLoginUser(request);
        if (null != loginUser) {
            long deptId = RedisCacheManager.getInstance().getRedisDeptId();
            // 记录用户退出日志
            sysLoginLogService.saveSysLoginInfo(loginUser.getUsername(), deptId,
                    SysStatus.LOGOUT_SUCCESS.getMessage(), OperateStatus.SUCCESS);
            // 删除用户缓存记录
            authTokenService.deleteToken(request);
        }
        BaseResult resp = BaseResult.ok();
        ServletExtUtils.responseJson(response, resp);
    }

}
