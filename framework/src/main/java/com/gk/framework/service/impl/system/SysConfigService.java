package com.gk.framework.service.impl.system;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.DataStatus;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.enums.YesOrNo;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.constant.RedisConstant;
import com.gk.framework.manager.RedisManager;
import com.gk.framework.mapper.system.SysConfigMapper;
import com.gk.framework.model.dto.system.sysConfig.*;
import com.gk.framework.model.entity.system.SysConfig;
import com.gk.framework.service.intf.system.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 配置表 服务实现类
 *
 * @author GuoYu
 * @since 2023-03-06 14:56:24
 */

@Service
@Slf4j
public class SysConfigService extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    @Resource
    private RedisManager redisManager;
    @Lazy
    @Resource
    private RedisCacheService redisCacheService;

    /**
     * 分页查询
     */
    @Override
    public IPage<SysConfigPageResp> pageList(SysConfigPageReq req) {
        return baseMapper.pageSysConfig(req.createPage(), req);
    }

    /**
     * 全表导出
     */
    @Override
    public List<SysConfigExportResp> exportList(SysConfigExportReq req) {
        return baseMapper.exportSysConfig(req);
    }

    /**
     * 新增
     */
    @Override
    public long add(SysConfigAddReq req) {
        SysConfig sysConfig = req.toEntity();
        if (checkExist(req.getConfigKey(), null)) {
            log.error("Add SysConfig Error: Add Config Key({}) already exist", req.getConfigKey());
            throw new SysException(SysStatus.CONFIG_KEY_ALREADY_EXIST);
        }
        if (!save(sysConfig)) {
            log.error("Add SysConfig Error: {}", JsonUtils.toJson(sysConfig));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        return sysConfig.getId();
    }

    /**
     * 修改
     */
    @Override
    @CacheEvict(value = RedisConstant.CONFIG_KEY, key = "#req.configKey")
    public void edit(SysConfigEditReq req) {
        // 校验配置是否合理
        checkEntity(req);

        // 校验配置是否存在
        if (checkExist(req.getConfigKey(), req.getId())) {
            log.error("Edit SysConfig Error: Edit Config Key({}) already exist", req.getConfigKey());
            throw new SysException(SysStatus.CONFIG_KEY_ALREADY_EXIST);
        }
        SysConfig sysConfig = req.toEntity();
        if (!updateById(sysConfig)) {
            log.error("Edit SysConfig Error: {}", JsonUtils.toJson(sysConfig));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 删除
     */
    @Override
    public void delete(List<Long> idList) {
        // 判断是否包含系统数据
        long systemDataCount =  lambdaQuery()
                .eq(SysConfig::getSystemData, YesOrNo.YES.value)
                .in(SysConfig::getId, idList)
                .count();
        if (0 != systemDataCount) {
            throw new SysException(SysStatus.SYSTEM_DATA_CAN_NOT_DELETE);
        }

        boolean result = lambdaUpdate()
                .in(SysConfig::getId, idList)
                .set(SysConfig::getEnabled, EnabledType.DISABLE.value)
                .update();
        if (!result) {
            log.error("Delete SysConfig Error: {}", idList);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 批量清除Redis缓存
        refreshCache(idList);
    }

    /**
     * 单个查询
     */
    @Override
    public SysConfigResp findOne(long id) {
        SysConfig sysConfig = lambdaQuery()
                .eq(SysConfig::getId, id)
                .eq(SysConfig::getEnabled, EnabledType.ENABLE.value)
                .one();
        if (sysConfig == null) {
            return null;
        }
        return sysConfig.toResp();
    }

    /**
     * 通过配置键来查询
     */
    @Override
    public SysConfig findByKey(String configKey) {
        return lambdaQuery().eq(SysConfig::getConfigKey, configKey)
                .eq(SysConfig::getDataStatus, DataStatus.NORMAL.value)
                .eq(SysConfig::getEnabled, EnabledType.ENABLE.value)
                .one();
    }

    /**
     * 通过配置键来查询
     */
    @Override
    public List<SysConfigPairResp> listConfigPair(List<String> configKeys) {
        return configKeys.stream()
                .map(configKey -> new SysConfigPairResp(configKey, redisCacheService.getConfigValue(configKey)))
                .collect(Collectors.toList());
    }

    /**
     * 批量清除Redis缓存
     *
     * @param idList 配置id集合 如果传入指定的id集合, 则清除指定的缓存, 如果传null, 则清除所有缓存
     */
    @Override
    public void refreshCache(List<Long> idList) {
        List<SysConfig> configList;
        if (CollUtil.isNotEmpty(idList)) {
            configList = lambdaQuery().in(SysConfig::getId, idList).list();
        } else {
            configList = lambdaQuery().eq(SysConfig::getDataStatus, DataStatus.NORMAL.value)
                    .eq(SysConfig::getEnabled, EnabledType.ENABLE.value).list();
        }
        configList.forEach(config -> redisManager.delete(RedisConstant.CONFIG_KEY + config.getConfigKey()));
    }

    // --------------------------- 私有方法 ---------------------------

    /**
     * 校验配置是否存在
     *
     * @param configKey   配置键
     * @param sysConfigId 配置id
     * @return true:已存在 false:不存在
     */
    private boolean checkExist(String configKey, Long sysConfigId) {
        LambdaQueryChainWrapper<SysConfig> qwConfig = lambdaQuery().eq(SysConfig::getConfigKey, configKey)
                .eq(SysConfig::getEnabled, EnabledType.ENABLE.value);
        // 修改时, 校验非当前配置键是否存在重复
        if (sysConfigId != null) {
            qwConfig.ne(SysConfig::getId, sysConfigId);
        }
        return CollUtil.isNotEmpty(qwConfig.list());
    }

    /**
     * 校验配置是否合理
     *
     * @param req 修改的配置
     */
    private void checkEntity(SysConfigEditReq req) {
        SysConfig dbEntity = getById(req.getId());
        if (YesOrNo.NO.value.equals(dbEntity.getSystemData())) {
            return;
        }
        // 校验配置键
        if (!dbEntity.getConfigKey().equals(req.getConfigKey())) {
            throw new SysException(SysStatus.SYSTEM_CONFIG_KEY_CAN_NOT_EDIT);
        }
        // 校验配置值
        if (YesOrNo.YES.value.equals(dbEntity.getConfigValue()) && YesOrNo.NO.value.equals(dbEntity.getConfigValue())) {
            throw new SysException(SysStatus.SYSTEM_CONFIG_VALUE_ILLEGAL);
        }
    }

}

