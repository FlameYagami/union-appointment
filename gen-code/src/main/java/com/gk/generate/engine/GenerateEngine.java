package com.gk.generate.engine;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.gk.generate.config.GenerateConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * 代码生成器
 *
 * @author GuoYu
 * @since 2022-11-17 16:28
 **/
@Slf4j
public class GenerateEngine {

    // ------------------------------------------- 主要生成配置 -------------------------------------------
    // 表名
    private static final String[] TABLE_NAMES          = {"xxl_job_info"};
    // 模块名
    private static final String   MODULE_NAME          = "server";
    // 模块文件夹名称(模块名与模块文件夹名称不同时才需要配置)
    private static final String   MODULE_NAME_DIR      = "";
    // 业务包名
    private static final String   SERVICE_PACKAGE_NAME = "demo";

    // ------------------------------------------- 自定义配置 -------------------------------------------
    /**
     * 数据库配置
     */
    public static final String DATASOURCE_URL      = "jdbc:mysql://192.168.0.88:3306/gk_admin?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&rewriteBatchedStatements=true";
    public static final String DATASOURCE_USERNAME = "root";
    public static final String DATASOURCE_PASSWORD = "Gkkj1009$";

    // 文件作者
    public static final  String AUTHOR       = "GuoYu";
    // 基础包名
    private static final String BASE_PACKAGE = "com.gk";

    // 表前缀
    private static final String[] TABLE_PREFIX = {""};
    // 表后缀
    private static final String[] TABLE_SUFFIX = {""};
    // 字段前缀
    private static final String[] FIELD_PREFIX = {""};
    // 字段后缀
    private static final String[] FIELD_SUFFIX = {""};

    /**
     * 生成代码直接执行main方法
     */
    public static void main(String[] args) {
        generateCode();
        System.out.println("-------------------------- 代码生成成功 --------------------------");
    }

    public static void generateCode() {
        GenerateConfig config = new GenerateConfig()
                .setAuthor(AUTHOR)
                .setBasePackage(BASE_PACKAGE)
                .setTableNames(TABLE_NAMES)
                .setModuleName(MODULE_NAME)
                .setModuleNameDir(MODULE_NAME_DIR)
                .setServicePackageName(SERVICE_PACKAGE_NAME)
                .setTablePrefix(TABLE_PREFIX)
                .setTableSuffix(TABLE_SUFFIX)
                .setFieldPrefix(FIELD_PREFIX)
                .setFieldSuffix(FIELD_SUFFIX);
        config.lateInit();
        FastAutoGenerator.create(DATASOURCE_URL, DATASOURCE_USERNAME, DATASOURCE_PASSWORD)
                .globalConfig(config::globalConfig)
                .packageConfig(config::packageConfig)
                .templateConfig(config::templateConfig)
                .strategyConfig(config::strategyConfig)
                .injectionConfig(config::injectionConfig)
                .templateEngine(new EnhanceVelocityTemplateEngine())
                .execute();
    }
}
