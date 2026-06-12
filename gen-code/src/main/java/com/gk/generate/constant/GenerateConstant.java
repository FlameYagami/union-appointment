package com.gk.generate.constant;

/**
 * 代码生成常量类
 *
 * @author GuoYu
 * @since 2022-11-15 16:16
 **/
public class GenerateConstant {

    private GenerateConstant() {
        throw new IllegalStateException("Constant class");
    }

    /**
     * 项目路径
     */
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    /**
     * 时间格式
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 代码基础路径
     */
    public static final String BASE_PATH = "/src/main";

    public static final String BASE_JAVA_PATH = "/java";
    public static final String BASE_RESOURCE_PATH = "/resources";

    public static final String ENTITY_PATH = "/model/entity/";
    public static final String DTO_PATH = "/model/dto/";
    public static final String MAPPER_PATH = "/mapper/";
    public static final String SERVICE_PATH = "/service/intf/";
    public static final String SERVICE_IMPL_PATH = "/service/impl/";
    public static final String CONTROLLER_PATH = "/controller/";

    public static final String MODEL = "model";

    /**
     * 字段名
     */
    public static final String COLUMN_CREATE_BY = "create_by";
    public static final String COLUMN_CREATE_TIME = "create_time";
    public static final String COLUMN_UPDATE_BY = "update_by";
    public static final String COLUMN_UPDATE_TIME = "update_time";
    public static final String COLUMN_ENABLED = "enabled";


}
