package com.gk.security.service.intf;

/**
 * Description:
 * Date: 2022/11/30 11:31
 * Author: Flame
 */
public interface ISysLoginService {

    /**
     * 登录
     *
     * @param username 账号
     * @param password 密码
     * @param aesKey AES 密钥
     * @param aesIv AES IV
     * @return Token令牌
     */
    String login(String username, String password, String aesKey, String aesIv);

}
