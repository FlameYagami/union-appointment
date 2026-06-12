package com.gk.common.utils;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.listener.base.BaseReadListener;
import com.gk.common.model.exception.SysException;
import com.gk.common.model.base.BaseExcelImportReq;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel工具类
 *
 * @author GuoYu
 * @since 2022-12-20 10:08
 **/
public class ExcelUtils {

    private ExcelUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 将列表以 Excel 响应给前端(固定表头)
     *
     * @param response  响应
     * @param sheetName Excel sheet 名
     * @param headClass Excel head 头
     * @param data      列表数据
     * @param <T>       泛型，保证 head 和 data 类型的一致性
     */
    public static <T> void write(HttpServletResponse response, String sheetName, Class<T> headClass, List<T> data) {
        try {
            StreamHeaderUtils.setupExcelHeader(response);
            EasyExcel.write(response.getOutputStream(), headClass)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 基于 column 长度，自动适配。最大 255 宽度
                    .sheet(sheetName).doWrite(data);
        } catch (Exception e) {
            throw new SysException(SysStatus.EXCEL_EXPORT_ERROR);
        }
    }

    /**
     * 将列表以 Excel 响应给前端(动态表头)
     *
     * @param response  响应
     * @param sheetName Excel sheet 名
     * @param headList  Excel head 头
     * @param data      列表数据
     * @param <T>       泛型，保证 head 和 data 类型的一致性
     */
    public static <T> void write(HttpServletResponse response, String sheetName, List<List<String>> headList, List<T> data) {
        try {
            StreamHeaderUtils.setupExcelHeader(response);
            EasyExcel.write(response.getOutputStream())
                    .head(headList)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet(sheetName).doWrite(data);
        } catch (Exception e) {
            throw new SysException(SysStatus.EXCEL_EXPORT_ERROR);
        }
    }

    /**
     * 把上传的Excel文件读取成列表
     *
     * @param file      上传的文件
     * @param headClass 表头类
     */
    public static <T> List<T> read(MultipartFile file, Class<T> headClass) {
        try {
            String fileExt = FileNameUtil.extName(file.getOriginalFilename());
            if (StrUtil.isNotEmpty(fileExt)
                    && (fileExt.equalsIgnoreCase(CommonConstant.EXT_EXCEL_XLS) || fileExt.equalsIgnoreCase(CommonConstant.EXT_EXCEL_XLSX))) {
                return EasyExcel.read(file.getInputStream(), headClass, null)
                        .doReadAllSync();
            } else {
                throw new SysException(SysStatus.FILE_IMPORT_TYPE_ERROR);
            }
        } catch (ExcelAnalysisException e) {
            throw new SysException(SysStatus.FILE_ANALYSIS_ERROR);
        } catch (IOException e) {
            throw new SysException(SysStatus.FILE_READ_ERROR);
        }
    }

    /**
     * 把上传的Excel文件读取到监听器中处理(自由度更高,可在监听器中做一些字段校验业务)
     *
     * @param file      上传的文件
     * @param headClass 表头类
     * @param listener  Excel导入监听器
     */
    public static <T> void read(MultipartFile file, Class<T> headClass, ReadListener<T> listener) {
        try {
            String fileExt = FileNameUtil.extName(file.getOriginalFilename());
            if (StrUtil.isNotEmpty(fileExt)
                    && (fileExt.equalsIgnoreCase(CommonConstant.EXT_EXCEL_XLS) || fileExt.equalsIgnoreCase(CommonConstant.EXT_EXCEL_XLSX))) {
                EasyExcel.read(file.getInputStream(), headClass, listener)
                        .sheet().doRead();
            } else {
                throw new SysException(SysStatus.FILE_IMPORT_TYPE_ERROR);
            }
        } catch (ExcelAnalysisException e) {
            throw new SysException(SysStatus.FILE_ANALYSIS_ERROR);
        } catch (IOException e) {
            throw new SysException(SysStatus.FILE_READ_ERROR);
        }
    }

    /**
     * 把上传的Excel文件读取到监听器中处理(基于上一个方法, 对listener做了封装, 简化代码的同时也可以更加专注于业务实现)
     *
     * @param file      上传的文件
     * @param headClass 表头类
     * @param listener  Excel导入监听器
     * @return 所有读取到的ExcelReq请求类
     */
    public static <T extends BaseExcelImportReq> List<T> read(MultipartFile file, Class<T> headClass, BaseReadListener<T> listener) {
        try {
            String fileExt = FileNameUtil.extName(file.getOriginalFilename());
            if (StrUtil.isNotEmpty(fileExt)
                    && (fileExt.equalsIgnoreCase(CommonConstant.EXT_EXCEL_XLS) || fileExt.equalsIgnoreCase(CommonConstant.EXT_EXCEL_XLSX))) {
                EasyExcel.read(file.getInputStream(), headClass, listener)
                        .sheet().doRead();
                return listener.getAllData();
            } else {
                throw new SysException(SysStatus.FILE_IMPORT_TYPE_ERROR);
            }
        } catch (ExcelAnalysisException e) {
            throw new SysException(SysStatus.FILE_ANALYSIS_ERROR);
        } catch (IOException e) {
            throw new SysException(SysStatus.FILE_READ_ERROR);
        }
    }

    /**
     * 检查Excel的表头是否正确
     *
     * @param headMap 检测到的表头Map
     * @param headClass  校验的表头class
     */
    public static <T> void checkExcelHead(Map<Integer, ReadCellData<?>> headMap, Class<T> headClass) {
        List<Field> fields = Arrays.stream(headClass.getDeclaredFields()).filter(o -> {
            ExcelIgnore excelIgnore = o.getAnnotation(ExcelIgnore.class);
            return excelIgnore == null;
        }).collect(Collectors.toList());
        if (headMap.size() != fields.size()) {
            throw new SysException(SysStatus.EXCEL_IMPORT_TEMPLATE_ERROR);
        }
        for (int i = 0; i < fields.size(); i++) {
            ExcelProperty excelProperty = fields.get(i).getAnnotation(ExcelProperty.class);
            if (headMap.get(i) == null || excelProperty == null) {
                continue;
            }
            String[] value = excelProperty.value();
            if (!headMap.get(i).getStringValue().contains(value[0])) {
                throw new SysException(SysStatus.EXCEL_IMPORT_TEMPLATE_ERROR);
            }
        }
    }
}
