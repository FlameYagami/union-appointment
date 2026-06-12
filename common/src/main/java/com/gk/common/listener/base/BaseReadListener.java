package com.gk.common.listener.base;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.gk.common.constant.ExcelErrorConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.base.BaseExcelImportReq;
import com.gk.common.model.exception.SysException;
import com.gk.common.model.others.ExcelImportError;
import com.gk.common.utils.ExcelUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel导入监听器基类
 *
 * @author Flame
 * @date 2023-05-09 17:02
 **/
public abstract class BaseReadListener<T extends BaseExcelImportReq> implements ReadListener<T> {

    private final Class<T> classType;

    /**
     * 所有导入的列表
     */
    @Getter
    private final List<T> allData = new ArrayList<>();

    /**
     * 错误提示列表
     */
    @Getter
    protected final List<ExcelImportError> errors = new ArrayList<>();

    /**
     * 单行数据校验
     *
     * @param req 单条Excel请求类
     */
    public abstract void validEachRow(T req);

    /**
     * 数据唯一性校验规则(如果返回null, 则不进行重复数据校验)
     *
     * @param req 单条Excel请求类
     * @return 唯一性校验规则
     */
    public abstract String validUnique(T req);

    /**
     * 所有数据读取完毕
     *
     * @param allList 全量的Excel请求类集合
     */
    public abstract void complete(List<T> allList);


    public BaseReadListener(Class<T> classType) {
        this.classType = classType;
    }

    /**
     * 表头数据读取回调
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        // 校验表头
        ExcelUtils.checkExcelHead(headMap, classType);
    }

    /**
     * 单条数据读取回调
     */
    @Override
    public void invoke(T req, AnalysisContext analysisContext) {
        if (req.isEmpty()) {
            return;
        }
        // 读取行号
        ReadRowHolder readRowHolder = analysisContext.readRowHolder();
        req.lateInit(readRowHolder.getRowIndex() + 1);
        allData.add(req);
        validEachRow(req);
    }

    /**
     * 全表数据读取完毕回调
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        validDuplicateData();
        // 校验表中数据合法性, 错误列表不为空说明数据校验有问题
        if (!errors.isEmpty()) {
            // 错误列表在业务实现类中处理及接口返回
            throw new SysException(SysStatus.IMPORT_FAILED, errors);
        }
        complete(allData);
    }

    /**
     * 校验重复项数据
     */
    private void validDuplicateData() {
        // 没有数则不校验
        if (allData.isEmpty()) {
            return;
        }
        // 数据唯一性校验规则(如果返回null, 则不进行重复数据校验)
        if (StrUtil.isEmpty(validUnique(allData.get(0)))) {
            return;
        }
        // 校验重复项, 并添加错误列表
        allData.stream().collect(Collectors.groupingBy(this::validUnique))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .flatMap(entry -> entry.getValue().stream())
                .forEach(excelReq -> errors.add(excelReq.toError(ExcelErrorConstant.DATA_REPEAT_ERROR)));
    }

}
