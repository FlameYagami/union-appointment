package com.gk.framework.utils;

import cn.hutool.core.util.StrUtil;
import com.gk.framework.model.dto.generate.GenColumnReq;
import com.gk.framework.model.dto.generate.GenReq;
import org.apache.velocity.VelocityContext;

import java.util.ArrayList;
import java.util.List;

public class VelocityUtils {

    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    public static VelocityContext prepareContext(GenReq req) {
        String businessName = StrUtil.toCamelCase(req.getTableName());
        String apiName = StrUtil.replace(req.getTableName(), "_", "-");
        String serviceName = StrUtil.upperFirst(businessName);
        List<GenColumnReq> columnList = req.getColumnList();
        columnList.forEach(columnReq -> columnReq.setJavaField(StrUtil.toCamelCase(columnReq.getColumnName())));

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tableName", req.getTableName());
        velocityContext.put("tableComment", req.getTableComment());
        velocityContext.put("moduleName", req.getModuleName());
        velocityContext.put("apiName", apiName);
        velocityContext.put("serviceName", serviceName);
        velocityContext.put("businessName", businessName);
        velocityContext.put("columns", columnList);
        return velocityContext;
    }

    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static List<String> getTemplatePathList() {
        List<String> templates = new ArrayList<>();
        templates.add("frontTemplate/list.html.vm");
        templates.add("frontTemplate/detail.html.vm");
        templates.add("frontTemplate/api.js.vm");
        return templates;
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String templatePath, VelocityContext context) {
        // 文件名称
        String fileName = "";

        String apiName = (String) context.get("apiName");
        Object moduleName = context.get("moduleName");

        StringBuilder vuePath = new StringBuilder("views/");
        if (moduleName != null) {
            vuePath.append((String) moduleName).append("/");
        }
        vuePath.append(apiName);

        StringBuilder apiPath = new StringBuilder("api/");
        if (moduleName != null) {
            apiPath.append((String) moduleName).append("/");
        } else {
            apiPath.append(apiName);
        }

        if (templatePath.contains("list.html.vm")) {
            fileName = StrUtil.format("{}/index.vue", vuePath.toString());
        } else if (templatePath.contains("detail.html.vm")) {
            fileName = StrUtil.format("{}/detail.vue", vuePath.toString());
        } else if (templatePath.contains("api.js.vm")) {
            fileName = StrUtil.format("{}/{}.js", apiPath.toString(), apiName);
        }
        return fileName;
    }

}
