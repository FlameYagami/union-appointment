package com.gk.framework.manager;

import cn.hutool.core.lang.Pair;
import cn.hutool.extra.spring.SpringUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.framework.constant.RedisConstant;
import com.gk.framework.helper.SysRoleHelper;
import com.gk.framework.model.dto.system.CachedUserData;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.service.intf.system.ISysUserDeptService;
import com.gk.framework.service.intf.system.ISysUserService;
import com.gk.framework.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存业务类
 *
 * @author GuoYu
 * @since 2023-01-22 15:36
 **/
@Slf4j
public class RedisCacheManager {

    private final RedisManager redisManager;
    private final ISysUserService sysUserService;
    private final ISysUserDeptService sysUserDeptService;

    public RedisCacheManager() {
        this.redisManager = SpringUtil.getBean(RedisManager.class);
        this.sysUserService = SpringUtil.getBean(ISysUserService.class);
        this.sysUserDeptService = SpringUtil.getBean(ISysUserDeptService.class);
    }

    public static RedisCacheManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final RedisCacheManager INSTANCE = new RedisCacheManager();
    }

    /**
     * 登录报错的锁定计数
     *
     * @param username 账号
     * @param errorCount 错误次数
     */
    public void loginError(String username, String errorCount) {
        redisManager.set(RedisConstant.LOGIN_ERROR_KEY + username, errorCount, 1, TimeUnit.DAYS);
    }

    /**
     * 获取登录报错的锁定次数
     *
     * @param username 账号
     * @return 锁定次数
     */
    public String getLoginErrorCount(String username) {
        return redisManager.get(RedisConstant.LOGIN_ERROR_KEY + username);
    }

    /**
     * 解锁账号
     *
     * @param username 账号
     */
    public void unlockAccount(String username) {
        redisManager.delete(RedisConstant.LOGIN_ERROR_KEY + username);
    }

    /**
     * 获取最后使用的角色id
     */
    public long getLastRoleId(long userId) {
        // 角色筛查
        List<Long> roleIds = SysRoleHelper.getInstance().getEnableRoleIdByUserId(userId);
        if (roleIds.isEmpty()) {
            log.error("Get Last Role Id Error: SysUser({}) has no role", userId);
            throw new SysException(SysStatus.ACCOUNT_HAS_NO_ROLE);
        }

        // 从redis查询这个账号最后一次适用的角色
        Long lastRoleId = null;
        String lastUserKey = RedisConstant.LAST_USER_KEY + userId;
        if (redisManager.hasHashKey(lastUserKey, RedisConstant.LAST_ROLE_KEY)) {
            lastRoleId = redisManager.getLongHashValue(lastUserKey, RedisConstant.LAST_ROLE_KEY);
        }

        // 角色存在直接返回
        if (null != lastRoleId && roleIds.contains(lastRoleId)) {
            return lastRoleId;
        }

        // 没有适用的最后角色默认取第一个
        long firstRoleId = roleIds.get(0);
        // 在redis缓存第一个角色
        redisManager.hashPut(lastUserKey, RedisConstant.LAST_ROLE_KEY, firstRoleId);
        return firstRoleId;
    }

    /**
     * 获取最后使用的部门id
     */
    public long getLastDeptId(long userId) {
        // 角色筛查
        SysUser sysUser = sysUserService.findByUserId(userId);

        // 部门添加
        List<Long> deptIds = sysUserDeptService.getEnableDeptIds(sysUser.getId());
        if (deptIds.isEmpty()) {
            log.error("Get Last Dept Id Error: SysUser({}) has no dept", userId);
            throw new SysException(SysStatus.ACCOUNT_HAS_NO_DEPT);
        }

        // 从redis查询这个账号最后一次适用的角色
        Long lastDeptId = null;
        String lastUserKey = RedisConstant.LAST_USER_KEY + userId;
        if (redisManager.hasHashKey(lastUserKey, RedisConstant.LAST_DEPT_KEY)) {
            lastDeptId = redisManager.getLongHashValue(lastUserKey, RedisConstant.LAST_DEPT_KEY);
        }

        // 角色存在直接返回
        if (null != lastDeptId && deptIds.contains(lastDeptId)) {
            return lastDeptId;
        }

        // 没有适用的最后角色默认取第一个
        long firstDeptId = deptIds.get(0);
        // 在redis缓存第一个部门
        redisManager.hashPut(lastUserKey, RedisConstant.LAST_DEPT_KEY, firstDeptId);
        return firstDeptId;
    }

    /**
     * 更新最后使用的角色id
     */
    public void updateLastRoleId(long userId, long roleId) {
        String lastUserKey = RedisConstant.LAST_USER_KEY + userId;
        redisManager.hashPut(lastUserKey, RedisConstant.LAST_ROLE_KEY, roleId);
    }

    /**
     * 更新最后使用的部门id
     */
    public void updateLastDeptId(long userId, long deptId) {
        String lastUserKey = RedisConstant.LAST_USER_KEY + userId;
        redisManager.hashPut(lastUserKey, RedisConstant.LAST_DEPT_KEY, deptId);
    }

    /**
     * 通过Token获取Redis中存储的Token令牌
     */
    public Map<String, Object> getRedisTokenData() {
        return redisManager.getHashEntries(TokenUtils.getTokenKey());
    }

    /**
     * 通过Token获取Redis中存储的Token令牌
     */
    public String getRedisToken() {
        String tokenKey = TokenUtils.getTokenKey();
        return redisManager.getHashValue(tokenKey, RedisConstant.TOKEN_HASH_KEY);
    }

    /**
     * 通过Token获取Redis中存储的Token令牌的过期时间戳
     */
    public long getRedisTokenExpired() {
        String tokenKey = TokenUtils.getTokenKey();
        return redisManager.getLongHashValue(tokenKey, RedisConstant.EXPIRED_HASH_KEY);
    }

    /**
     * 通过Token获取Redis中存储的当前角色
     */
    public long getRedisRoleId() {
        String tokenKey = TokenUtils.getTokenKey();
        Long roleId = redisManager.getLongHashValue(tokenKey, RedisConstant.ROLE_HASH_KEY);
        if (null == roleId) {
            return CommonConstant.NA_VALUE;
        }
        return roleId;
    }

    /**
     * 通过Token获取Redis中存储的当前部门
     */
    public long getRedisDeptId() {
        String tokenKey = TokenUtils.getTokenKey();
        Long deptId = redisManager.getLongHashValue(tokenKey, RedisConstant.DEPT_HASH_KEY);
        if (null == deptId) {
            return CommonConstant.NA_VALUE;
        }
        return deptId;
    }

    /**
     * 通过Token获取当前用户的AES密钥
     */
    public Pair<String, String> getRedisAesPair() {
        String tokenKey = TokenUtils.getTokenKey();
        String aesKey = redisManager.getHashValue(tokenKey, RedisConstant.AES_KEY_HASH_KEY);
        String aesIv = redisManager.getHashValue(tokenKey, RedisConstant.AES_IV_HASH_KEY);
        return Pair.of(aesKey, aesIv);
    }

    /**
     * 更新Token获取Redis中存储的当前角色
     */
    public void updateRedisRoleId(long roleId) {
        String tokenKey = TokenUtils.getTokenKey();
        redisManager.hashPut(tokenKey, RedisConstant.ROLE_HASH_KEY, roleId);
    }

    /**
     * 更新Redis中存储的当前部门
     */
    public void updateRedisDeptId(long deptId) {
        String tokenKey = TokenUtils.getTokenKey();
        redisManager.hashPut(tokenKey, RedisConstant.DEPT_HASH_KEY, deptId);
    }

    /**
     * 更新当前用户的AES key
     */
    public void updateRedisAesKey(String aesKey) {
        String tokenKey = TokenUtils.getTokenKey();
        redisManager.hashPut(tokenKey, RedisConstant.AES_KEY_HASH_KEY, aesKey);
    }

    /**
     * 更新当前用户的AES iv
     */
    public void updateRedisAesIv(String aesIv) {
        String tokenKey = TokenUtils.getTokenKey();
        redisManager.hashPut(tokenKey, RedisConstant.AES_IV_HASH_KEY, aesIv);
    }

    /**
     * 用户密码变更(重置密码后需要变更)
     */
    public void alterPassword(long userId, String encryptPassword) {
        String userInfoKey = RedisConstant.USER_INFO_KEY + userId;
        CachedUserData cachedUserData = redisManager.get(userInfoKey);
        cachedUserData.setPassword(encryptPassword);
        redisManager.set(userInfoKey, cachedUserData);
    }

}
