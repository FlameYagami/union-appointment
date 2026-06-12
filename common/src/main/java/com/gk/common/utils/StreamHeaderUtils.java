package com.gk.common.utils;

import javax.servlet.http.HttpServletResponse;

/**
 * 输出流响应头工具类
 *
 * @author GuoYu
 * @since 2021-08-30 08:42
 **/
public class StreamHeaderUtils {
    private StreamHeaderUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 配置Excel的响应输出流
     *
     * @param response 响应
     */
    public static void setupExcelHeader(HttpServletResponse response) {
        response.addHeader("Content-Disposition", "attachment;filename=xxx.xlsx");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
    }

    /**
     * 配置PDF的响应输出流
     *
     * @param response 响应
     */
    public static void setupPdfHeader(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment;filename=xxx.pdf");
        response.setContentType("application/pdf");
        response.setCharacterEncoding("utf-8");
    }

    /**
     * 配置PDF的响应输出流
     *
     * @param response 响应
     * @param data 响应数据
     */
    public static void setupZipHeader(HttpServletResponse response, byte[] data) {
        response.setHeader("Content-Disposition", "attachment;filename=xxx.zip");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
    }
}
