package com.gk.common.utils;

import cn.hutool.core.collection.CollUtil;
import com.gk.common.enums.EnabledType;
import com.gk.common.model.base.BaseEntity;
import com.gk.common.model.base.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 数据库关联操作辅助工具类
 *
 * @author Flame
 * @since 2023-02-20 8:51
 **/
public class EntityUtils {

    /**
     * 关联关系id接口
     */
    public interface IRelIdBuilder<T extends BaseEntity> {
        /**
         * 获取关系id
         */
        long getRelId(T t);
    }

    /**
     * 关联主键id接口
     */
    public interface IPrimaryIdBuilder<T extends BaseEntity> {
        /**
         * 获取主键id
         */
        Long getId(T t);
    }

    /**
     * 关联关系联合key接口
     */
    public interface IUnionKeyBuilder<T extends BaseEntity> {

        /**
         * 获取联合key
         */
        String getRelUnionKey(T t);
    }

    /**
     * 关联关系联合key接口
     */
    public interface IEntityUnionKeyBuilder<T extends BaseTimeEntity> {

        /**
         * 获取联合key
         */
        String getRelUnionKey(T t);
    }

    /**
     * 新增关系(增量数据) 生成最后保存的关系集合(适用于只有两个关系id的关联表, 无需包含主键id) <br/>
     * 此方法会自动查找出与数据库的差异, 比对出已存在但停用了的关系, 将新增的对应关系与重新启用的关系自动合并成一个最终集合
     *
     * @param saveList      新增关系集合(增量数据)
     * @param dbList        数据库查询出的关系集合
     * @param iRelIdBuilder 需要进行对比的关系id的lambda表达式
     */
    public static <T extends BaseEntity> List<T> generateAddRelEntity(List<T> saveList, List<T> dbList, IRelIdBuilder<T> iRelIdBuilder) {
        // 整理需要保存的关联id合集
        List<Long> saveRelIds = saveList.stream()
                .map(iRelIdBuilder::getRelId)
                .collect(Collectors.toList());

        // 把数据库中的重复数据给去除掉
        List<T> distinctDbList = CollUtil.distinct(dbList, iRelIdBuilder::getRelId, false);

        // 查询数据库与请求中重叠的数据
        List<T> repeatEntities = distinctDbList.stream()
                .filter(entity -> saveRelIds.contains(iRelIdBuilder.getRelId(entity)))
                .peek(entity -> entity.setEnabled(EnabledType.ENABLE.value))
                .collect(Collectors.toList());

        // 整理重叠数据中关联id集合
        List<Long> repeatRelIds = repeatEntities.stream()
                .map(iRelIdBuilder::getRelId)
                .collect(Collectors.toList());

        // 查询需要新增的数据
        List<T> addEntities = saveList.stream()
                .filter(entity -> !repeatRelIds.contains(iRelIdBuilder.getRelId(entity)))
                .collect(Collectors.toList());

        // 构建所有保存的数据
        List<T> finallyEntities = new ArrayList<>();
        finallyEntities.addAll(addEntities);
        finallyEntities.addAll(repeatEntities);
        return finallyEntities;
    }

    /**
     * 修改关系(全量数据) 生成最后保存的关系集合(适用于只有两个关系id的关联表, 无需包含主键id) <br/>
     * 此方法会自动查找出与数据库的差异, 将新增/删除对应关系与未改动的关系自动合并成一个最终集合
     *
     * @param saveList      请求中转换成的数据库模型集合(全量数据)
     * @param dbList        数据库查询出的相关集合
     * @param iRelIdBuilder 获取需要调整的关系id的lambda表达式
     */
    public static <T extends BaseEntity> List<T> generateRelEntity(List<T> saveList, List<T> dbList, IRelIdBuilder<T> iRelIdBuilder) {
        // 整理需要保存的关联id合集
        List<Long> saveRelIds = saveList.stream()
                .map(iRelIdBuilder::getRelId)
                .collect(Collectors.toList());

        // 筛选数据库中启用与要保存的一致的数据
        List<Long> enabledRelIds = dbList.stream()
                .filter(entity -> EnabledType.ENABLE.value.equals(entity.getEnabled()))
                .filter(entity -> saveRelIds.contains(iRelIdBuilder.getRelId(entity)))
                .map(iRelIdBuilder::getRelId)
                .collect(Collectors.toList());

        // 查询数据库与请求中重叠的数据并设置为启用状态
        List<T> repeatEntities = dbList.stream()
                .filter(entity -> saveRelIds.contains(iRelIdBuilder.getRelId(entity)))
                .filter(entity -> !enabledRelIds.contains(iRelIdBuilder.getRelId(entity))) // 排除已经启用的
                .peek(entity -> entity.setEnabled(EnabledType.ENABLE.value))
                .collect(Collectors.toList());

        // 整理重叠数据中关联id集合
        List<Long> repeatRelIds = dbList.stream()
                .filter(entity -> saveRelIds.contains(iRelIdBuilder.getRelId(entity)))
                .map(iRelIdBuilder::getRelId)
                .collect(Collectors.toList());

        // 筛选数据库中弃用与要保存中不匹配的数据
        List<Long> disabledRelIds = dbList.stream()
                .filter(entity -> EnabledType.DISABLE.value.equals(entity.getEnabled()))
                .filter(entity -> !saveRelIds.contains(iRelIdBuilder.getRelId(entity)))
                .map(iRelIdBuilder::getRelId)
                .collect(Collectors.toList());

        // 查询删除的数据
        List<T> deleteEntities = dbList.stream()
                .filter(entity -> !repeatRelIds.contains(iRelIdBuilder.getRelId(entity)))
                .filter(entity -> !disabledRelIds.contains(iRelIdBuilder.getRelId(entity))) // 排除已经弃用的
                .peek(entity -> entity.setEnabled(EnabledType.DISABLE.value))
                .collect(Collectors.toList());

        // 查询需要新增的数据
        List<T> addEntities = saveList.stream()
                .filter(entity -> !repeatRelIds.contains(iRelIdBuilder.getRelId(entity)))
                .collect(Collectors.toList());

        // 构建所有保存的数据
        List<T> finallyEntities = new ArrayList<>();
        finallyEntities.addAll(addEntities);
        finallyEntities.addAll(repeatEntities);
        finallyEntities.addAll(deleteEntities);
        return finallyEntities;
    }

    /**
     * 修改关系(全量数据) 生成最后保存的关系集合(适用于联合主键修改)
     * 此方法会自动查找出与数据库的差异, 将新增/删除对应关系与未改动的关系自动合并成一个最终集合
     *
     * @param saveList         请求中转换成的数据库模型集合(全量数据)
     * @param dbList           数据库查询出的相关集合
     * @param iUnionKeyBuilder 获取需要调整的关系联合key的lambda表达式
     * @param repeatConsumer   为重复的模型提供一个修改属性的入口
     */
    public static <T extends BaseEntity> List<T> generateAllEntity(List<T> saveList, List<T> dbList, IUnionKeyBuilder<T> iUnionKeyBuilder, Consumer<T> repeatConsumer) {
        // 整理需要保存的关联联合key合集
        List<String> saveUnionRelKeys = saveList.stream()
                .map(iUnionKeyBuilder::getRelUnionKey)
                .collect(Collectors.toList());

        // 查询数据库与请求中重叠的数据
        List<T> repeatEntities = dbList.stream()
                .filter(entity -> saveUnionRelKeys.contains(iUnionKeyBuilder.getRelUnionKey(entity)))
                .peek(entity -> {
                    entity.setEnabled(EnabledType.ENABLE.value);
                    repeatConsumer.accept(entity);
                }).collect(Collectors.toList());

        // 整理重叠数据中关联联合key集合
        List<String> repeatUnionKeys = repeatEntities.stream()
                .map(iUnionKeyBuilder::getRelUnionKey)
                .collect(Collectors.toList());

        // 查询删除的数据
        List<T> deleteEntities = dbList.stream()
                .filter(entity -> !repeatUnionKeys.contains(iUnionKeyBuilder.getRelUnionKey(entity)))
                .peek(entity -> entity.setEnabled(EnabledType.DISABLE.value)).collect(Collectors.toList());

        // 查询需要新增的数据
        List<T> addEntities = saveList.stream()
                .filter(entity -> !repeatUnionKeys.contains(iUnionKeyBuilder.getRelUnionKey(entity)))
                .collect(Collectors.toList());

        // 构建所有保存的数据
        List<T> finallyEntities = new ArrayList<>();
        finallyEntities.addAll(addEntities);
        finallyEntities.addAll(repeatEntities);
        finallyEntities.addAll(deleteEntities);
        return finallyEntities;
    }

    /**
     * 修改关系(全量数据) 生成最后保存的关系集合(适用于联合主键修改)
     * 此方法会自动查找出与数据库的差异, 将新增/删除对应关系与未改动的关系自动合并成一个最终集合
     *
     * @param saveList         请求中转换成的数据库模型集合(全量数据)
     * @param dbList           数据库查询出的相关集合
     * @param iUnionKeyBuilder 获取需要调整的关系联合key的lambda表达式
     * @param repeatConsumer   为重复的模型提供一个修改属性的入口
     */
    public static <T extends BaseTimeEntity> List<T> generateAllEntity(List<T> saveList, List<T> dbList, IEntityUnionKeyBuilder<T> iUnionKeyBuilder, Consumer<T> repeatConsumer) {
        // 整理需要保存的关联联合key合集
        List<String> saveUnionRelKeys = saveList.stream()
                .map(iUnionKeyBuilder::getRelUnionKey)
                .collect(Collectors.toList());

        // 查询数据库与请求中重叠的数据
        List<T> repeatEntities = dbList.stream()
                .filter(entity -> saveUnionRelKeys.contains(iUnionKeyBuilder.getRelUnionKey(entity)))
                .peek(entity -> {
                    entity.setEnabled(EnabledType.ENABLE.value);
                    repeatConsumer.accept(entity);
                }).collect(Collectors.toList());

        // 整理重叠数据中关联联合key集合
        List<String> repeatUnionKeys = repeatEntities.stream()
                .map(iUnionKeyBuilder::getRelUnionKey)
                .collect(Collectors.toList());

        // 查询删除的数据
        List<T> deleteEntities = dbList.stream()
                .filter(entity -> !repeatUnionKeys.contains(iUnionKeyBuilder.getRelUnionKey(entity)))
                .peek(entity -> entity.setEnabled(EnabledType.DISABLE.value)).collect(Collectors.toList());

        // 查询需要新增的数据
        List<T> addEntities = saveList.stream()
                .filter(entity -> !repeatUnionKeys.contains(iUnionKeyBuilder.getRelUnionKey(entity)))
                .collect(Collectors.toList());

        // 构建所有保存的数据
        List<T> finallyEntities = new ArrayList<>();
        finallyEntities.addAll(addEntities);
        finallyEntities.addAll(repeatEntities);
        finallyEntities.addAll(deleteEntities);
        return finallyEntities;
    }

    /**
     * 生成最后保存的关联集合(一对多关联子项操作, 需要包含主键id) <br/>
     * 此方法会根据主键id查找出与数据库的差异, 将新增/修改/删除对应关系与未改动的关系自动合并成一个最终集合
     *
     * @param saveList          请求中转换成的数据库模型集合(全量数据)
     * @param dbList            数据库查询出的相关集合
     * @param iPrimaryIdBuilder 获取主键id的lambda表达式
     */
    public static <T extends BaseEntity> List<T> generateRelEntityList(List<T> saveList, List<T> dbList, IPrimaryIdBuilder<T> iPrimaryIdBuilder) {
        List<T> resultList = new ArrayList<>(saveList);
        // 数据库中列表与传入列表比对 得到删除列表
        List<T> removeList = dbList.stream()
                .filter(dbItem -> saveList.stream()
                        .filter(reqItem -> iPrimaryIdBuilder.getId(reqItem) != null)
                        .noneMatch(reqItem -> iPrimaryIdBuilder.getId(dbItem).equals(iPrimaryIdBuilder.getId(reqItem))))
                .peek(dbItem -> dbItem.setEnabled(EnabledType.DISABLE.value))
                .collect(Collectors.toList());

        resultList.addAll(removeList);
        return resultList;
    }

}
