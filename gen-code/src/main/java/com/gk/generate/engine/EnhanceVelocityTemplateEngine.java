package com.gk.generate.engine;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 代码生成器模板配置引擎
 *
 * @author GuoYu
 * @since 2022-12-05 16:46
 **/
public final class EnhanceVelocityTemplateEngine extends VelocityTemplateEngine {

    /**
     * Map映射配置
     */
    @Override
    @NotNull
    public Map<String, Object> getObjectMap(@NotNull ConfigBuilder config, @NotNull TableInfo tableInfo) {
        // 获取实体类名字
        // 获取object map
        Map<String, Object> objectMap = super.getObjectMap(config, tableInfo);
        objectMap.put("lowerService", StrUtil.lowerFirst(tableInfo.getServiceImplName()));
        String tableComment = tableInfo.getComment();
        int lastIndex = tableComment.lastIndexOf("表");
        if (lastIndex != -1) {
            tableComment = tableComment.substring(0, lastIndex);
        }
        objectMap.put("tableComment", tableComment);
        // 提取表名缩写
        String[] tableNameParts = tableInfo.getName().split("_");
        String tableAbbr = Arrays.stream(tableNameParts).map(it ->it.substring(0, 1)).collect(Collectors.joining());
        objectMap.put("tableAbbr", tableAbbr);

        // 字段备注处理
        Map<String, String> fieldCommentMap = new HashMap<>();
        Map<String, String> fieldDefValueMap = new HashMap<>();
        tableInfo.getFields().forEach(it -> {
            // 去除表字段备注()内信息, 例如: 数据状态(1:正常, 0:停用, 默认:1) => 数据状态
            fieldCommentMap.put(it.getPropertyName(), it.getComment().replaceAll("\\s*\\([^)]*\\)", ""));
            // 使用正则匹配数字, 例如: 数据状态(1:正常, 0:停用, 默认:1) => 1, 提取第一个数字
            List<String> result = ReUtil.findAll("\\d+", it.getComment(), 0);
            fieldDefValueMap.put(it.getPropertyName(), result.isEmpty()? it.getComment(): result.get(0));
        });
        objectMap.put("fieldCommentMap", fieldCommentMap);
        objectMap.put("fieldDefValueMap", fieldDefValueMap);
        return objectMap;
    }

    /**
     * 文件输出路径
     *
     * @param customFile 自定义文件map
     * @param tableInfo  表信息
     * @param objectMap  对象map
     */
    @Override
    protected void outputCustomFile(@NotNull Map<String, String> customFile, @NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        // 获取实体类名字
        String entityName = tableInfo.getEntityName();
        String lowerEntity = tableInfo.getEntityPath();
        // 获取other包盘符路径
        String otherPath = this.getPathInfo(OutputFile.other);
        // 输出自定义java模板
        customFile.forEach((key, value) -> {
            // 输出路径
            String fileName = otherPath + File.separator + lowerEntity + File.separator + entityName + key;
            this.outputFile(new File(fileName), objectMap, value, true);
        });
    }

}
