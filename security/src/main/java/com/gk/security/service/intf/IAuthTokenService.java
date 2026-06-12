package com.gk.security.service.intf;

import com.gk.framework.model.bo.security.LoginUser;

import javax.servlet.http.HttpServletRequest;

/**
 * Token认证业务接口
 *
 * @author GuoYu
 * @since 2023-01-12 16:22
 */
public interface IAuthTokenService {

    /**
     * 创建令牌
     */
    String createToken(long userId, long roleId, long deptId, String aesKey, String aesIv);

    /**
     * 验证令牌，Token有效期不足10分钟自动续期
     */
    void verifyToken(HttpServletRequest request);

    /**
     * 刷新令牌有效期
     */
    void refreshToken(String token);

    /**
     * 删除Token令牌
     */
    void deleteToken(HttpServletRequest request);

    /**
     * 获取用户身份信息
     */
    LoginUser findLoginUser(HttpServletRequest request);
}
