package com.gk.generate.engine;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static java.io.File.separator;

/**
 * 项目重构修改器，一键改包
 *
 * @author GuoYu
 * @date 2023-01-12 16:11
 */
@Slf4j
public class ProjectReactor {

    // 项目名称
    private static final String PROJECT_NAME = "union-appointment";
    // Gradle中的模块前缀
    private static final String GROUP_ID = "admin.gk";
    // 基础包名
    private static final String PACKAGE_NAME = "com.gk";
    // 中文描述 一般用在Swagger中
    private static final String TITLE = "工会预约管理系统";

    /**
     * 白名单文件，不进行重写，避免出问题
     */
    private static final Set<String> WHITE_FILE_TYPES = new HashSet<>(Arrays.asList(
            "gif", "jpg", "svg", "png", // 图片
            "json", // json文件
            "xdb", // ip检测库
            "eot", "woff2", "ttf", "ttc", "fon", "woff")); // 字体

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String projectBaseDir = getProjectBaseDir();
        log.error("原项目路径改地址: {}", projectBaseDir);


        // ============================== 配置，需要你手动修改 ==============================
        // 新项目名称
        String projectNameNew = "union-appointment";
        // Gradle中的模块前缀
        String groupIdNew = "admin.gk";
        // 基础包名
        String packageNameNew = "com.gk";
        // 中文描述 一般用在Swagger中
        String titleNew = "工会预约管理系统";
        // ============================== 配置，需要你手动修改 ==============================


        // 一键改名后，新项目所在的目录
        String projectBaseDirNew = projectBaseDir.replace(PROJECT_NAME, projectNameNew);
        log.error("检测新项目目录({})是否存在", projectBaseDirNew);
        if (FileUtil.exist(projectBaseDirNew)) {
            log.error("新项目目录检测 ({})已存在，请更改新的目录！程序结束", projectBaseDirNew);
            return;
        }

        // 如果新目录中存在 PACKAGE_NAME，ARTIFACT_ID 等关键字，路径会被替换，导致生成的文件不在预期目录
        if (StrUtil.containsAny(projectBaseDirNew, PACKAGE_NAME, PROJECT_NAME, StrUtil.upperFirst(PROJECT_NAME))) {
            log.error("新项目目录检测 ({}) 存在冲突名称「{}」或者「{}」，请更改新的目录！程序结束", projectBaseDirNew, PACKAGE_NAME, PROJECT_NAME);
            return;
        }
        log.error("完成新项目目录检测，新项目路径地址: {}", projectBaseDirNew);
        // 获得需要复制的文件
        log.error("开始获得需要重写的文件, 请等待...");
        Collection<File> files = listFiles(projectBaseDir);
        log.error("需要重写的文件数量：{}，请等待...", files.size());
        // 写入文件
        files.forEach(file -> {
            // 如果是白名单的文件类型，不进行重写，直接拷贝
            String fileType = FileNameUtil.extName(file.getName());
            if (WHITE_FILE_TYPES.contains(fileType)) {
                copyFile(file, projectBaseDir, projectBaseDirNew, packageNameNew, projectNameNew);
                return;
            }
            // 如果非白名单的文件类型，重写内容，在生成文件
            String content = replaceFileContent(file, groupIdNew, projectNameNew, packageNameNew, titleNew);
            writeFile(file, content, projectBaseDir, projectBaseDirNew, packageNameNew, projectNameNew);
        });

        // 拷贝标准化的.idea文件夹至新项目
        URL ideaConfigUrl = ResourceUtil.getResource("ideaConfig");
        String destPath = projectBaseDirNew + separator;
        FileUtil.copy(ideaConfigUrl.getPath(), destPath, true);
        File sourceFile = new File(destPath + "ideaConfig");
        FileUtil.rename(sourceFile, ".idea", true);
        log.error("执行完成, 共耗时：{} 秒", (System.currentTimeMillis() - start) / 1000);
    }

    private static String getProjectBaseDir() {
        String baseDir = System.getProperty("user.dir");
        if (StrUtil.isEmpty(baseDir)) {
            throw new NullPointerException("项目基础路径不存在");
        }
        return baseDir;
    }

    private static Collection<File> listFiles(String projectBaseDir) {
        Collection<File> files = FileUtils.listFiles(new File(projectBaseDir), null, true);
        // 移除 IDEA、Git 自身的文件、Node 编译出来的文件
        files = files.stream()
                .filter(file -> !file.getPath().contains(separator + "target" + separator)
                        && !file.getPath().contains(separator + "build" + separator)
                        && !file.getPath().contains(separator + "node_modules" + separator)
                        && !file.getPath().contains(separator + ".idea" + separator)
                        && !file.getPath().contains(separator + ".git" + separator)
                        && !file.getPath().contains(separator + ".gradle" + separator)
                        && !file.getPath().contains(separator + "dist" + separator)
                        && !file.getPath().contains(".iml")
                        && !file.getPath().contains(".DS_Store")
                        && !file.getPath().contains(".html.gz"))
                .collect(Collectors.toList());
        return files;
    }

    private static String replaceFileContent(File file, String groupIdNew,
                                             String artifactIdNew, String packageNameNew,
                                             String titleNew) {
        String content = FileUtil.readString(file, StandardCharsets.UTF_8);
        // 如果是白名单的文件类型，不进行重写
        String fileType = FileNameUtil.extName(file.getName());
        if (WHITE_FILE_TYPES.contains(fileType)) {
            return content;
        }
        // 执行文件内容都重写 ARTIFACT_ID必须放在最后替换，因为 ARTIFACT_ID 太短！
        return content.replaceAll(GROUP_ID, groupIdNew)
                .replaceAll(PACKAGE_NAME, packageNameNew)
                .replaceAll(PROJECT_NAME, artifactIdNew)
                .replaceAll(StrUtil.upperFirst(PROJECT_NAME), StrUtil.upperFirst(artifactIdNew))
                .replaceAll(TITLE, titleNew);
    }

    private static void writeFile(File file, String fileContent, String projectBaseDir,
                                  String projectBaseDirNew, String packageNameNew, String artifactIdNew) {
        String newPath = buildNewFilePath(file, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
        FileUtil.writeUtf8String(fileContent, newPath);
    }

    private static void copyFile(File file, String projectBaseDir,
                                 String projectBaseDirNew, String packageNameNew, String artifactIdNew) {
        String newPath = buildNewFilePath(file, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
        FileUtil.copyFile(file, new File(newPath));
    }

    private static String buildNewFilePath(File file, String projectBaseDir,
                                           String projectBaseDirNew, String packageNameNew, String artifactIdNew) {
        // 新目录
        return file.getPath().replace(projectBaseDir, projectBaseDirNew)
                .replace(PACKAGE_NAME.replaceAll("\\.", Matcher.quoteReplacement(separator)),
                        packageNameNew.replaceAll("\\.", Matcher.quoteReplacement(separator)))
                .replace(PROJECT_NAME, artifactIdNew)
                .replaceAll(StrUtil.upperFirst(PROJECT_NAME), StrUtil.upperFirst(artifactIdNew));
    }

}
