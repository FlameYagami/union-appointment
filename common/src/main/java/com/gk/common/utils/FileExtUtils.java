package com.gk.common.utils;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 文件操作工具类
 *
 * @author Flame
 * @date 2020-05-19
 **/
@Slf4j
public class FileExtUtils extends FileUtil {

    private FileExtUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean writeFile(byte[] fileBytes, String desPath) {
        try {
            File file = new File(desPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(fileBytes);
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            log.error("Write File Error: {}", e.getMessage());
            return false;
        }
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }

        log.error("Delete File Error: file path({}) is not exist", filePath);
        return false;
    }
}
