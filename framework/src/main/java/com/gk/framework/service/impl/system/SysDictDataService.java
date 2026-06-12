package com.gk.framework.service.impl.system;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.DataStatus;
import com.gk.framework.model.dto.system.base.DataStatusChangeReq;
import com.gk.framework.model.dto.system.sysDictData.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.constant.RedisConstant;
import com.gk.framework.manager.DictCacheManager;
import com.gk.framework.mapper.system.SysDictDataMapper;
import com.gk.framework.model.entity.system.SysDictData;
import com.gk.framework.service.intf.system.ISysDictDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典数据表 服务实现类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */

@Service
@Slf4j
public class SysDictDataService extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {

    /**
     * 刷新字典的本地缓存<br/>
     * 延迟初始化
     */
    @PostConstruct
    @Override
    public synchronized void refreshLocalCache() {
        if (!DictCacheManager.getDict().isEmpty()) {
            DictCacheManager.getDict().clear();
        }
        List<SysDictData> dbDictMap = lambdaQuery()
                .select(SysDictData::getDictCode, SysDictData::getDictLabel, SysDictData::getDictValue)
                .eq(SysDictData::getEnabled, EnabledType.ENABLE.value)
                .list();

        Map<String, Map<String, String>> dbDictDataMap = dbDictMap.stream()
                .collect(Collectors.groupingBy(SysDictData::getDictCode, Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel)));
        dbDictDataMap.forEach((key, value) -> {
            BiMap<String, String> biMap = HashBiMap.create(value);
            DictCacheManager.getDict().put(key, biMap);
        });
        log.info("RefreshLocalCache dict cache size ({})", DictCacheManager.getDict().size());
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<SysDictDataPageResp> pageList(SysDictDataPageReq req) {
        return baseMapper.pageSysDictData(req.createPage(), req);
    }

    /**
     * 全表导出
     */
    @Override
    public List<SysDictDataExportResp> exportList(SysDictDataExportReq req) {
        return baseMapper.exportSysDictData(req);
    }

    /**
     * 通过字典编码查询所有对应的字典项
     */
    @Override
    public List<SysDictData> listByDictCode(String dictCode) {
        return lambdaQuery().eq(SysDictData::getDictCode, dictCode)
                .eq(SysDictData::getEnabled, EnabledType.ENABLE.value)
                .orderByAsc(SysDictData::getDictOrder)
                .list();
    }

    /**
     * 新增
     */
    @Override
    @CacheEvict(value = RedisConstant.DICT_DATA_KEY, key = "#req.dictCode")
    public long add(SysDictDataAddReq req) {
        SysDictData sysDictData = req.toEntity();
        if (checkExist(req.getDictCode(), req.getDictLabel(), req.getDictValue(), null)) {
            log.error("Add SysDictData Error: Add Dict Code({}), Dict Label({}) or Dict Value({}) already exist", req.getDictCode(), req.getDictLabel(), req.getDictValue());
            throw new SysException(SysStatus.DICT_DATA_LABEL_VALUE_EXIST);
        }
        if (!save(sysDictData)) {
            log.error("Add SysDictData Error: {}", JsonUtils.toJson(sysDictData));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        DictCacheManager.saveDictData(req.getDictCode(), req.getDictValue(), req.getDictLabel(), null);
        return sysDictData.getId();
    }

    /**
     * 批量新增导入
     */
    @Override
    @CacheEvict(value = RedisConstant.DICT_DATA_KEY, key = "#dictCode")
    public void importBatch(String dictCode, List<SysDictData> dictDataList) {
        if (!saveBatch(dictDataList, dictDataList.size())) {
            log.error("Import Excel SysDictData List Error: {}", JsonUtils.toJson(dictDataList));
            throw new SysException(SysStatus.EXCEL_IMPORT_ERROR);
        }
    }

    /**
     * 修改
     */
    @Override
    @CacheEvict(value = RedisConstant.DICT_DATA_KEY, key = "#req.dictCode")
    public void edit(SysDictDataEditReq req) {
        SysDictData sysDictData = req.toEntity();
        if (checkExist(req.getDictCode(), req.getDictLabel(), req.getDictValue(), req.getId())) {
            log.error("Edit SysDictData Error: Edit Dict Code({}), Dict Label({}) or Dict Value({}) already exist", req.getDictCode(), req.getDictLabel(), req.getDictValue());
            throw new SysException(SysStatus.DICT_DATA_LABEL_VALUE_EXIST);
        }
        SysDictData dbDictData = lambdaQuery().eq(SysDictData::getId, req.getId())
                .eq(SysDictData::getEnabled, EnabledType.ENABLE.value).one();
        if (dbDictData == null) {
            throw new SysException(SysStatus.ID_NOT_FOUND);
        }
        if (!dbDictData.getDictCode().equals(sysDictData.getDictCode())) {
            throw new SysException(SysStatus.DICT_DATA_CODE_CAN_NOT_EDIT);
        }
        if (!updateById(sysDictData)) {
            log.error("Edit SysDictData Error: {}", JsonUtils.toJson(sysDictData));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        DictCacheManager.saveDictData(req.getDictCode(), req.getDictValue(), req.getDictLabel(), dbDictData);
    }

    /**
     * 字典编码修改, 字典项编码全部变更
     */
    @Override
    @CacheEvict(value = RedisConstant.DICT_DATA_KEY, key = "#originDictCode")
    public void editDictCode(String originDictCode, String dictCode) {
        if (StrUtil.isEmpty(originDictCode) || StrUtil.isEmpty(dictCode)) {
            return;
        }
        lambdaUpdate().eq(SysDictData::getDictCode, originDictCode)
                .eq(SysDictData::getEnabled, EnabledType.ENABLE.value)
                .set(SysDictData::getDictCode, dictCode)
                .update();

        DictCacheManager.removeDictCode(originDictCode);
        SpringUtil.getBean(ISysDictDataService.class).refreshCache(dictCode);
    }

    /**
     * 删除
     */
    @Override
    @CacheEvict(value = RedisConstant.DICT_DATA_KEY, key = "#dictCode")
    public void delete(List<Long> idList, String dictCode) {
        boolean result = lambdaUpdate().in(SysDictData::getId, idList)
                .set(SysDictData::getEnabled, EnabledType.DISABLE.value)
                .update();
        if (!result) {
            log.error("Delete SysDictData Error: {}", idList);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        List<String> dictValueList = baseMapper.listDictValueById(idList);
        DictCacheManager.deleteDictData(dictCode, dictValueList);
    }

    /**
     * 刷新单个字典的缓存
     */
    @Override
    @CacheEvict(value = RedisConstant.DICT_DATA_KEY, key = "#dictCode")
    public void refreshCache(String dictCode) {
        List<SysDictData> dictDataList = lambdaQuery()
                .eq(SysDictData::getDictCode, dictCode)
                .eq(SysDictData::getEnabled, EnabledType.ENABLE.value)
                .eq(SysDictData::getDataStatus, DataStatus.NORMAL.value)
                .list();
        DictCacheManager.deleteAllDictData(dictCode);
        BiMap<String, String> biMap = DictCacheManager.getDict().computeIfAbsent(dictCode, k -> HashBiMap.create());
        for (SysDictData dictData : dictDataList) {
            biMap.put(dictData.getDictValue(), dictData.getDictLabel());
        }
        log.info("Refresh dict cache, dict code: {}", dictCode);
    }

    /**
     * 根据字典编码删除全部字典项
     *
     * @param dictCode 字典编码
     */
    @Override
    @CacheEvict(value = RedisConstant.DICT_DATA_KEY, key = "#dictCode")
    public void deleteAllByDictCode(String dictCode) {
        lambdaUpdate().eq(SysDictData::getDictCode, dictCode)
                .set(SysDictData::getEnabled, EnabledType.DISABLE.value)
                .update();
        DictCacheManager.deleteAllDictData(dictCode);
    }

    /**
     * 单个查询
     */
    @Override
    public SysDictDataResp findOne(long id) {
        SysDictData sysDictData = lambdaQuery()
                .eq(SysDictData::getId, id)
                .eq(SysDictData::getEnabled, EnabledType.ENABLE.value)
                .one();
        if (sysDictData == null) {
            return null;
        }
        return sysDictData.toResp();
    }

    /**
     * 修改字典数据状态
     */
    @Override
    public void changeDataStatus(DataStatusChangeReq req) {
        boolean result = lambdaUpdate()
                .set(SysDictData::getDataStatus, req.getDataStatus())
                .eq(SysDictData::getId, req.getId())
                .update();
        if (!result) {
            log.error("Change DataStatus Error in SysDictData: id({})", req.getId());
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 重新载入字典的数据
        SysDictData dbData = getById(req.getId());
        SpringUtil.getBean(ISysDictDataService.class).refreshCache(dbData.getDictCode());
    }

    // --------------------------- 私有方法 ---------------------------
    /**
     * 校验字典项是否存在
     *
     * @param dictCode 字典编码
     * @param dictLabel 字典标签
     * @param dictValue 字典值
     * @param sysDictDataId 字典项id
     * @return true:已存在 false:不存在
     */
    private boolean checkExist(String dictCode, String dictLabel, String dictValue, Long sysDictDataId) {
        LambdaQueryChainWrapper<SysDictData> qwDictData = lambdaQuery().eq(SysDictData::getDictCode, dictCode)
                .eq(SysDictData::getEnabled, EnabledType.ENABLE.value)
                .and(wrapper -> wrapper.eq(SysDictData::getDictLabel, dictLabel).or().eq(SysDictData::getDictValue, dictValue));
        // 修改时, 校验非当前字典是否重名或重复编码
        if (sysDictDataId != null) {
            qwDictData.ne(SysDictData::getId, sysDictDataId);
        }
        List<SysDictData> dictDataList = qwDictData.list();
        return CollUtil.isNotEmpty(dictDataList);
    }

}

