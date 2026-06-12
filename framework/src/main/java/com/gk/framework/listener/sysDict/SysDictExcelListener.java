package com.gk.framework.listener.sysDict;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gk.common.constant.ExcelErrorConstant;
import com.gk.common.listener.base.BaseReadListener;
import com.gk.framework.model.dto.system.sysDict.SysDictExcelReq;
import com.gk.framework.service.intf.system.ISysDictService;

import java.util.List;

/**
 * 示例表 Excel导入监听器
 *
 * @author GuoYu
 * @since 2023-04-26 15:43
 **/
public class SysDictExcelListener extends BaseReadListener<SysDictExcelReq> {

    private final ISysDictService sysDictService;

    /** 数据库已有的字典编码集合 */
    private final List<String> dbDictCodeList;


    public SysDictExcelListener(ISysDictService sysDictService, List<String> dbDictCodeList) {
        super(SysDictExcelReq.class);
        this.sysDictService = sysDictService;
        this.dbDictCodeList = dbDictCodeList;
    }

    @Override
    public void validEachRow(SysDictExcelReq req) {
        // 必填项校验
        if (StrUtil.isEmpty(req.getDictName())) {
            errors.add(req.toError(ExcelErrorConstant.DICT_NAME_EMPTY_ERROR));
        }
        if (StrUtil.isEmpty(req.getDictCode())) {
            errors.add(req.toError(ExcelErrorConstant.DICT_CODE_EMPTY_ERROR));
        }
        if (StrUtil.isEmpty(req.getDictLabel())) {
            errors.add(req.toError(ExcelErrorConstant.DICT_LABEL_EMPTY_ERROR));
        }
        if (StrUtil.isEmpty(req.getDictValue())) {
            errors.add(req.toError(ExcelErrorConstant.DICT_VALUE_EMPTY_ERROR));
        }
        if (req.getDictOrder() == null) {
            errors.add(req.toError(ExcelErrorConstant.DICT_ORDER_EMPTY_ERROR));
        }
        if (CollUtil.contains(dbDictCodeList, req.getDictCode())) {
            errors.add(req.toError(ExcelErrorConstant.DICT_CODE_ALREADY_EXIST));
        }
    }

    @Override
    public String validUnique(SysDictExcelReq req) {
        return req.getDictCode() + req.getDictLabel() + req.getDictValue();
    }

    @Override
    public void complete(List<SysDictExcelReq> allList) {
        // 调用业务实现类中保存Excel数据入数据库的方法
        sysDictService.saveExcel(allList);
    }


}
