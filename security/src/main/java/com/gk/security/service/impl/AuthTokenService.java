package com.gk.security.service.impl;

import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.AesUtils;
import com.gk.framework.constant.RedisConstant;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.manager.RedisManager;
import com.gk.framework.model.bo.security.LoginUser;
import com.gk.framework.model.bo.security.TokenInfo;
import com.gk.framework.model.dto.system.CachedUserData;
import com.gk.framework.service.intf.system.IRedisCacheService;
import com.gk.framework.utils.TokenUtils;
import com.gk.security.constant.SecurityConstant;
import com.gk.security.service.intf.IAuthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Token认证业务实现类
 *
 * @author GuoYu
 * @since 2023-01-13 10:31
 */

@Service
@Slf4j
public class AuthTokenService implements IAuthTokenService {

    @Resource
    private RedisManager redisManager;

    @Resource
    private IRedisCacheService redisCacheService;

    /**
     * 令牌有效期（单位:分钟）
     */
    @Value("${token.expireTime}")
    private long expireTime;

    /**
     * 创建令牌
     */
    @Override
    public String createToken(long userId, long roleId, long deptId, String aesKey, String aesIv) {
        long currentTimestamp = System.currentTimeMillis();
        String clearToken = currentTimestamp + CommonConstant.MINUS + userId;
        String token = AesUtils.encrypt(clearToken, CommonConstant.DEFAULT_AES_KEY, CommonConstant.DEFAULT_AES_IV);

        Map<String, Object> tokenMap = new HashMap<>(6);
        tokenMap.put(RedisConstant.TOKEN_HASH_KEY, token);
        tokenMap.put(RedisConstant.EXPIRED_HASH_KEY, currentTimestamp + expireTime);
        tokenMap.put(RedisConstant.ROLE_HASH_KEY, roleId);
        tokenMap.put(RedisConstant.DEPT_HASH_KEY, deptId);
        tokenMap.put(RedisConstant.AES_KEY_HASH_KEY, aesKey);
        tokenMap.put(RedisConstant.AES_IV_HASH_KEY, aesIv);
        String tokenKey = TokenUtils.getTokenKey(userId, currentTimestamp);
        // 存储Token并设置过期时间
        redisManager.hashPutAll(tokenKey, tokenMap);
        redisManager.expire(tokenKey, expireTime, TimeUnit.MINUTES);

        return token;
    }

    /**
     * 验证令牌，Token有效期不足10分钟自动续期
     */
    @Override
    public void verifyToken(HttpServletRequest request) {
        String token = TokenUtils.getRequestToken(request);
        Map<String, Object> tokenData = RedisCacheManager.getInstance().getRedisTokenData();
        if (tokenData == null || tokenData.isEmpty()) {
            log.info("Auth Token Error: redis token data is null or empty");
            throw new SysException(SysStatus.UNAUTHORIZED_ERROR);
        }
        String redisToken = (String) tokenData.get(RedisConstant.TOKEN_HASH_KEY);
        if (!redisToken.equals(token)) {
            log.error("Auth Token Error: request token and redis token is not equal");
            throw new SysException(SysStatus.UNAUTHORIZED_ERROR);
        }
        long expireTime = (long) tokenData.get(RedisConstant.EXPIRED_HASH_KEY);
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= SecurityConstant.MILLIS_ONE_DAY) {
            refreshToken(token);
        }
    }

    /**
     * 刷新令牌有效期
     */
    @Override
    public void refreshToken(String token) {
        TokenInfo tokenInfo = TokenUtils.getTokenInfo(token);
        if (tokenInfo == null) {
            return;
        }
        long refreshTimestamp = System.currentTimeMillis() + expireTime;
        String tokenKey = TokenUtils.getTokenKey(tokenInfo);
        redisManager.hashPut(tokenKey, RedisConstant.EXPIRED_HASH_KEY, refreshTimestamp);
        redisManager.expire(tokenKey, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 删除Token令牌
     */
    @Override
    public void deleteToken(HttpServletRequest request) {
        TokenInfo tokenInfo = TokenUtils.getTokenInfo(request);
        if (tokenInfo != null) {
            String tokenKey = TokenUtils.getTokenKey(tokenInfo);
            redisManager.delete(tokenKey);
        }
    }

    /**
     * 获取用户身份信息
     */
    @Override
    public LoginUser findLoginUser(HttpServletRequest request) {
        TokenInfo tokenInfo = TokenUtils.getTokenInfo(request);
        if (tokenInfo == null) {
            return null;
        }
        CachedUserData cachedUserData = redisCacheService.getUserData(tokenInfo.getUserId());
        return new LoginUser(cachedUserData);
    }

}
