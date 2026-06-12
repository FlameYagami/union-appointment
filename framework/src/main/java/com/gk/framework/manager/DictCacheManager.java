package com.gk.framework.manager;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.gk.framework.model.entity.system.SysDictData;

import java.util.ArrayList;
import java.util.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典缓存管理类
 *
 * @author GuoYu
 * @since 2023-02-22 10:03
 **/
public class DictCacheManager {

    private DictCacheManager() {
        throw new IllegalStateException("Utility manager class");
    }


    /**
     * 字典缓存
     * 按照dictCode 只缓存dictValue及dictLabel
     * BiMap作为双向Map, 可直接通过key取value, 也可通过value取key
     */
    private static final Map<String, BiMap<String, String>> DICT_CACHE = new HashMap<>();

    /**
     * 获取缓存字典
     */
    public static Map<String, BiMap<String, String>> getDict() {
        return DICT_CACHE;
    }

    /**
     * 保存或更新DictCache中的字典项缓存
     *
     * @param dictCode 字典编码
     * @param dictValue 字典值
     * @param dictLabel 字典标签
     */
    public static void saveDictData(String dictCode, String dictValue, String dictLabel, SysDictData oldDictData) {
        BiMap<String, String> biMap = DICT_CACHE.computeIfAbsent(dictCode, k -> HashBiMap.create());

        if (oldDictData != null && !oldDictData.getDictValue().equals(dictValue)) {
            biMap.remove(oldDictData.getDictValue());
        }
        biMap.put(dictValue, dictLabel);
    }

    /**
     * 导入新的字典
     *
     * @param dictCode 字典编码
     * @param dictDataList 字典项集合
     */
    public static void importDictDataBatch(String dictCode, List<SysDictData> dictDataList) {
        BiMap<String, String> biMap = HashBiMap.create();
        dictDataList.forEach(dictData -> biMap.put(dictData.getDictValue(), dictData.getDictLabel()));
        DICT_CACHE.put(dictCode, biMap);
    }

    /**
     * DictCache中删除对应的字典项缓存
     *
     * @param dictCode 字典编码
     * @param dictValueList 字典值集合
     */
    public static void deleteDictData(String dictCode, List<String> dictValueList) {
        BiMap<String, String> biMap = DICT_CACHE.get(dictCode);
        if (biMap == null) {
            return;
        }
        for (String dictValue : dictValueList) {
            biMap.remove(dictValue);
        }
    }

    /**
     * DictCache中清空对应字典编码的所有字典项缓存
     *
     * @param dictCode 字典编码
     */
    public static void deleteAllDictData(String dictCode) {
        BiMap<String, String> biMap = DICT_CACHE.get(dictCode);
        if (biMap == null) {
            return;
        }
        biMap.clear();
    }

    /**
     * DictCache中移除对应字典编码的字典缓存
     *
     * @param dictCode 字典编码
     */
    public static void removeDictCode(String dictCode) {
        DICT_CACHE.remove(dictCode);
    }

    /**
     * 从DictCache缓存中获取字典文本标签
     *
     * @param dictCode 字典编码
     * @param dictValue 字典值
     */
    public static String getDictLabel(String dictCode, String dictValue) {
        BiMap<String, String> biMap = DICT_CACHE.get(dictCode);
        if (biMap == null) {
            return "";
        }
        return biMap.get(dictValue);
    }

    /**
     * 从DictCache缓存中获取字典值
     *
     * @param dictCode 字典编码
     * @param dictLabel 字典文本标签
     */
    public static String getDictValue(String dictCode, String dictLabel) {
        BiMap<String, String> biMap = DICT_CACHE.get(dictCode);
        if (biMap == null) {
            return "";
        }
        return biMap.inverse().get(dictLabel);
    }

    /**
     * 从DictCache缓存中获取对应字典编码的所有字典值
     *
     * @param dictCode 字典编码
     */
    public static List<String> getDictAllValues(String dictCode) {
        BiMap<String, String> biMap = DICT_CACHE.get(dictCode);
        if (biMap == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(biMap.keySet());
    }
}
