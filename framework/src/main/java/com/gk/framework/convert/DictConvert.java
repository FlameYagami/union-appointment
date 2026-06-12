package com.gk.framework.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.gk.common.constant.CommonConstant;
import com.gk.common.utils.StringExtUtils;
import com.gk.framework.annotation.DictFormat;
import com.gk.framework.manager.DictCacheManager;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel 数据字典转换器
 *
 * @author GuoYu
 * @since 2023-02-22 09:50
 */
@Slf4j
public class DictConvert implements Converter<Object> {

    @Override
    public Class<?> supportJavaTypeKey() {
        throw new UnsupportedOperationException("暂不支持，也不需要");
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        throw new UnsupportedOperationException("暂不支持，也不需要");
    }

    @Override
    public Object convertToJavaData(ReadCellData readCellData, ExcelContentProperty contentProperty,
                                    GlobalConfiguration globalConfiguration) {
        // 使用字典解析
        String dictCode = getDictCode(contentProperty);
        String label = readCellData.getStringValue();
        String value = CommonConstant.EMPTY;
        if (StrUtil.isNotEmpty(label)) {
            String dictValue = DictCacheManager.getDictValue(dictCode, label);
            if (StrUtil.isNotEmpty(dictValue)) {
                value = dictValue;
            }
        }
        // 将 String 的 value 转换成对应的属性
        Class<?> fieldClazz = contentProperty.getField().getType();
        return Convert.convert(fieldClazz, value);
    }

    @Override
    public WriteCellData<String> convertToExcelData(Object object, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        if (object == null) {
            return new WriteCellData<>("");
        }

        // 使用字典格式化
        String dictCode = getDictCode(contentProperty);
        String valueStr = String.valueOf(object);
        if (StrUtil.isEmpty(valueStr)) {
            return new WriteCellData<>(CommonConstant.NO_CONTENT);
        }
        List<String> valueList = StrUtil.split(valueStr, CommonConstant.COMMA, true, true);
        List<String> labelList = new ArrayList<>();
        for (String value : valueList) {
            String label = DictCacheManager.getDictLabel(dictCode, value);
            if (StrUtil.isNotEmpty(label)) {
                labelList.add(label);
            }
        }
        if (CollUtil.isEmpty(labelList)) {
            return new WriteCellData<>(CommonConstant.NO_CONTENT);
        }

        // 生成 Excel 小表格
        return new WriteCellData<>(StringExtUtils.joinComma(labelList));
    }

    private static String getDictCode(ExcelContentProperty contentProperty) {
        return contentProperty.getField().getAnnotation(DictFormat.class).value();
    }

}
