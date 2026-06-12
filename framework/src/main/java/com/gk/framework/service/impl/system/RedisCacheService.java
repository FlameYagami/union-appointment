package com.gk.framework.service.impl.system;

import com.gk.common.enums.DataStatus;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.framework.constant.RedisConstant;
import com.gk.framework.model.bo.system.sysOpenConfig.OpenConfigCache;
import com.gk.framework.model.dto.system.CachedDictData;
import com.gk.framework.model.dto.system.CachedUserData;
import com.gk.framework.model.entity.system.SysConfig;
import com.gk.framework.model.entity.system.SysDictData;
import com.gk.framework.model.entity.system.SysOpenConfig;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.service.intf.system.IRedisCacheService;
import com.gk.framework.service.intf.system.ISysConfigService;
import com.gk.framework.service.intf.system.ISysDictDataService;
import com.gk.framework.service.intf.system.ISysOpenConfigService;
import com.gk.framework.service.intf.system.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Redis缓存 服务实现类
 *
 * @author Flame
 * @date 2023-04-28 15:33
 **/
@Service
@Slf4j
public class RedisCacheService implements IRedisCacheService {

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysDictDataService sysDictDataService;

    @Resource
    private ISysConfigService sysConfigService;

    @Resource
    private ISysOpenConfigService sysOpenConfigService;

    /**
     * 查询用户缓存信息
     *
     * @param userId 用户id
     */
    @Cacheable(value = RedisConstant.USER_INFO_KEY, key = "#userId", unless = "#result == null")
    @Override
    public CachedUserData getUserData(long userId) {
        SysUser sysUser = sysUserService.findByUserId(userId);
        if (sysUser == null) {
            log.error("Get Cached User Data Error: sysUser not found by userId: {}", userId);
            throw new SysException(SysStatus.UNAUTHORIZED_USER_INFO);
        }
        return new CachedUserData(sysUser);
    }

    /**
     * 查询字典缓存信息
     *
     * @param dictCode 字典编码
     */
    @Cacheable(value = RedisConstant.DICT_DATA_KEY, key = "#dictCode", unless = "#result == null || #result.isEmpty()")
    @Override
    public List<CachedDictData> getDictData(String dictCode) {
        return sysDictDataService.listByDictCode(dictCode).stream()
                .filter(it -> DataStatus.NORMAL.value.equals(it.getDataStatus())) // 排除停用的字典数据
                .map(SysDictData::toCache)
                .collect(Collectors.toList());
    }

    /**
     * 查询配置值缓存
     *
     * @param configKey 配置键
     */
    @Cacheable(value = RedisConstant.CONFIG_KEY, key = "#configKey", unless = "#result == null || #result.isEmpty()")
    @Override
    public String getConfigValue(String configKey) {
        SysConfig sysConfig = sysConfigService.findByKey(configKey);
        if (sysConfig == null) {
            return null;
        }
        return sysConfig.getConfigValue();
    }

    /**
     * 查询用户缓存信息
     */
    @Cacheable(value = RedisConstant.OPEN_CONFIG_KEY, key = "#openId", unless = "#result == null")
    @Override
    public OpenConfigCache getOpenConfig(String openId) {
        SysOpenConfig sysOpenConfig = sysOpenConfigService.findByOpenId(openId);
        if (null == sysOpenConfig) {
            return null;
        }
        return sysOpenConfig.toCache();
    }

}
