package com.gk.framework.utils;

import cn.hutool.core.util.StrUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.AesUtils;
import com.gk.common.utils.ServletExtUtils;
import com.gk.framework.constant.RedisConstant;
import com.gk.framework.model.bo.security.TokenInfo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * Token令牌工具类
 *
 * @author GuoYu
 * @since 2023-01-13 15:56
 */
@Slf4j
public class TokenUtils {

    private TokenUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 从请求中获取Token令牌
     */
    public static String getRequestToken() {
        HttpServletRequest request = ServletExtUtils.getRequest();
        return getRequestToken(request);
    }

    /**
     * 从请求中获取Token令牌
     * @param request 请求
     */
    public static String getRequestToken(HttpServletRequest request) {
        String token = request.getHeader(CommonConstant.TOKEN_HEADER);
        if (StrUtil.isNotEmpty(token) && token.startsWith(CommonConstant.TOKEN_PREFIX)) {
            token = token.replace(CommonConstant.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * 解析Token中所含信息
     * @param request 请求
     */
    public static TokenInfo getTokenInfo(HttpServletRequest request) {
        return getTokenInfo(getRequestToken(request));
    }

    /**
     * 解析Token中所含信息
     * @param token 认证令牌
     */
    public static TokenInfo getTokenInfo(String token) {
        if (StrUtil.isEmpty(token)) {
            return null;
        }

        try {
            String clearToken = AesUtils.decrypt(token, CommonConstant.DEFAULT_AES_KEY, CommonConstant.DEFAULT_AES_IV);
            if (StrUtil.isEmpty(clearToken)) {
                return null;
            }
            String[] tokenArray = clearToken.split(CommonConstant.MINUS);
            // Token分为生成时的时间戳及userId两部分
            if (tokenArray.length < 2) {
                return null;
            }
            long timestamp = Long.parseLong(tokenArray[0]);
            long userId = Long.parseLong(tokenArray[1]);

            return new TokenInfo()
                    .setTimestamp(timestamp)
                    .setUserId(userId);
        } catch (Exception e) {
            log.error("Token decrypt error: " + e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * 获取Redis中存储Token信息的key
     */
    public static String getTokenKey() {
        return getTokenKey(ServletExtUtils.getRequest());
    }

    /**
     * 获取Redis中存储Token信息的key
     * @param request 请求
     */
    public static String getTokenKey(HttpServletRequest request) {
        TokenInfo tokenInfo = getTokenInfo(getRequestToken(request));
        if (tokenInfo == null) {
            throw new SysException(SysStatus.UNAUTHORIZED_USER_INFO);
        }
        return getTokenKey(tokenInfo);
    }

    /**
     * 获取Redis中存储Token信息的key
     * @param tokenInfo token信息类
     */
    public static String getTokenKey(TokenInfo tokenInfo) {
        return RedisConstant.LOGIN_TOKEN_KEY + tokenInfo.getUserId() + CommonConstant.UNDERLINE + tokenInfo.getTimestamp();
    }

    /**
     * 获取Redis中存储Token信息的key
     * @param userId 用户ID
     * @param timestamp token生成时的时间戳
     */
    public static String getTokenKey(long userId, long timestamp) {
        return RedisConstant.LOGIN_TOKEN_KEY + userId + CommonConstant.UNDERLINE + timestamp;
    }

}
