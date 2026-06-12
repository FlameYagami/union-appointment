package com.gk.generate.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.gk.generate.constant.GenerateConstant;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GuoYu
 * @since 2022-11-15 16:20
 **/
@Data
@Accessors(chain = true)
public class GenerateConfig {

    // ------------------------------------------- 自定义配置 -------------------------------------------
    // 文件作者
    public String author;

    // 基础包名
    private String basePackage;
    // 基础路径
    private String domainPath;
    // 模块名
    private String moduleName;
    // 模块文件夹名称
    private String moduleNameDir;
    // 业务包名
    private String servicePackageName;

    // 表名
    private String[] tableNames;
    // 表前缀
    private String[] tablePrefix;
    // 表后缀
    private String[] tableSuffix;
    // 字段前缀
    private String[] fieldPrefix;
    // 字段后缀
    private String[] fieldSuffix;

    // 代码生成路径
    private String genPath;
    // java路径
    private String javaPath;
    // resource路径
    private String resourcePath;

    private String entityPath;
    private String mapperPath;
    private String servicePath;
    private String serviceImplPath;
    private String controllerPath;
    private String xmlPath;
    // DTO路径
    private String otherPath;

    /**
     * 生成器参数配置
     */
    public void lateInit() {
        // 域名包路径
        this.domainPath = File.separator + this.basePackage.replace(".", File.separator);

        if (StrUtil.isEmpty(moduleNameDir)) {
            moduleNameDir = moduleName;
        }

        // 代码生成路径
        this.genPath = GenerateConstant.PROJECT_PATH + File.separator + moduleNameDir + GenerateConstant.BASE_PATH;
        // java路径
        this.javaPath = genPath + GenerateConstant.BASE_JAVA_PATH + domainPath + File.separator + moduleName;
        // resource路径
        this.resourcePath = genPath + GenerateConstant.BASE_RESOURCE_PATH;

        this.entityPath = javaPath + GenerateConstant.ENTITY_PATH + servicePackageName;
        this.mapperPath = javaPath + GenerateConstant.MAPPER_PATH + servicePackageName;
        this.servicePath = javaPath + GenerateConstant.SERVICE_PATH + servicePackageName;
        this.serviceImplPath = javaPath + GenerateConstant.SERVICE_IMPL_PATH + servicePackageName;
        this.controllerPath = javaPath + GenerateConstant.CONTROLLER_PATH + servicePackageName;
        this.xmlPath = resourcePath + GenerateConstant.MAPPER_PATH + servicePackageName;
        // DTO路径
        this.otherPath = javaPath + GenerateConstant.DTO_PATH + servicePackageName;
    }

    /**
     * 全局配置
     */
    public void globalConfig(GlobalConfig.Builder builder) {
        builder.author(author)
                .commentDate(GenerateConstant.DATE_PATTERN)
                .dateType(DateType.ONLY_DATE)
                .enableSwagger()
                .disableOpenDir(); // 禁止打开输出目录
    }

    /**
     * 模板配置
     */
    public void templateConfig(TemplateConfig.Builder builder) {
        builder.entity("/templates/entity.java")
                .mapper("/templates/mapper.java")
                .service("/templates/service.java")
                .serviceImpl("/templates/serviceImpl.java")
                .controller("/templates/controller.java")
                .xml("/templates/mapper.xml");
    }

    /**
     * 包配置
     */
    public void packageConfig(PackageConfig.Builder builder) {
        builder.parent(basePackage)
                .moduleName(moduleName)
                .entity("model.entity")
                .mapper("mapper")
                .service("service.intf")
                .serviceImpl("service.impl")
                .controller("controller")
                .other("model.dto")
                .pathInfo(configPathInfo());
    }

    /**
     * 策略配置
     */
    public void strategyConfig(StrategyConfig.Builder builder) {
        builder.addTablePrefix(tablePrefix)
                .addTableSuffix(tableSuffix)
                .addFieldPrefix(fieldPrefix)
                .addFieldSuffix(fieldSuffix)
                .addInclude(tableNames)

                // Entity 策略配置
                .entityBuilder()
                .enableLombok() // 开启lombok
                .enableChainModel() // 链式
                .enableRemoveIsPrefix() // 开启boolean类型字段移除is前缀
                .enableTableFieldAnnotation() //开启生成实体时生成的字段注解
                .naming(NamingStrategy.underline_to_camel) // 表名 下划线 -》 驼峰命名
                .columnNaming(NamingStrategy.underline_to_camel) // 字段名 下划线 -》 驼峰命名
                .formatFileName("%s") // Entity 文件名称
                .enableColumnConstant()
                .addIgnoreColumns(GenerateConstant.COLUMN_CREATE_BY,
                        GenerateConstant.COLUMN_CREATE_TIME,
                        GenerateConstant.COLUMN_UPDATE_BY,
                        GenerateConstant.COLUMN_UPDATE_TIME,
                        GenerateConstant.COLUMN_ENABLED)
//                .fileOverride() // 生成时覆盖已有文件

                // Controller 策略配置
                .controllerBuilder()
                .enableRestStyle() // 开启@RestController
                .formatFileName("%sController") // Controller 文件名称
//                .fileOverride() // 生成时覆盖已有文件

                // Service 策略配置
                .serviceBuilder()
                .formatServiceFileName("I%sService") // Service 文件名称
                .formatServiceImplFileName("%sService") // ServiceImpl 文件名称
//                .fileOverride() // 生成时覆盖已有文件

                // Mapper 策略配置
                .mapperBuilder()
                .enableBaseColumnList() // 启用 columnList (通用查询结果列)
                .enableBaseResultMap() // 启动resultMap
                .formatMapperFileName("%sMapper") // Mapper 文件名称
                .formatXmlFileName("%sMapper"); // Xml 文件名称
//                .fileOverride(); // 生成时覆盖已有文件

    }

    /**
     * 注入配置
     */
    public void injectionConfig(InjectionConfig.Builder builder) {

        Map<String, Object> customMap = new HashMap<>();
        Map<String, String> customFile = new HashMap<>();
        // DTO
        customFile.put("BaseReq.java", "/templates/dto/baseReq.java.vm");
        customFile.put("AddReq.java", "/templates/dto/addReq.java.vm");
        customFile.put("EditReq.java", "/templates/dto/editReq.java.vm");
        customFile.put("Resp.java", "/templates/dto/resp.java.vm");
        customFile.put("PageReq.java", "/templates/dto/pageReq.java.vm");
        customFile.put("PageResp.java", "/templates/dto/pageResp.java.vm");
        customFile.put("ExportReq.java", "/templates/dto/exportReq.java.vm");
        customFile.put("ExportResp.java", "/templates/dto/exportResp.java.vm");
        builder.customFile(customFile);
        customMap.put("basePackage", basePackage);
        customMap.put("module", moduleName);
        customMap.put("model", GenerateConstant.MODEL);
        customMap.put("servicePackage", servicePackageName);
        builder.customMap(customMap);
    }

    /**
     * 路径
     */
    private Map<OutputFile, String> configPathInfo() {
        Map<OutputFile, String> pathInfo = new HashMap<>(7);
        pathInfo.put(OutputFile.entity, entityPath);
        pathInfo.put(OutputFile.mapper, mapperPath);
        pathInfo.put(OutputFile.service, servicePath);
        pathInfo.put(OutputFile.serviceImpl, serviceImplPath);
        pathInfo.put(OutputFile.controller, controllerPath);
        pathInfo.put(OutputFile.xml, xmlPath);
        pathInfo.put(OutputFile.other, otherPath);
        return pathInfo;
    }
}
