package com.gk.framework.service.impl.system;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.ExcelUtils;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.constant.RedisConstant;
import com.gk.framework.listener.sysDict.SysDictExcelListener;
import com.gk.framework.manager.DictCacheManager;
import com.gk.framework.manager.RedisManager;
import com.gk.framework.mapper.system.SysDictMapper;
import com.gk.framework.model.dto.system.sysDict.SysDictAddReq;
import com.gk.framework.model.dto.system.sysDict.SysDictEditReq;
import com.gk.framework.model.dto.system.sysDict.SysDictExcelReq;
import com.gk.framework.model.dto.system.sysDict.SysDictExportReq;
import com.gk.framework.model.dto.system.sysDict.SysDictExportResp;
import com.gk.framework.model.dto.system.sysDict.SysDictPageReq;
import com.gk.framework.model.dto.system.sysDict.SysDictPageResp;
import com.gk.framework.model.dto.system.sysDict.SysDictResp;
import com.gk.framework.model.entity.system.SysDict;
import com.gk.framework.model.entity.system.SysDictData;
import com.gk.framework.service.intf.system.ISysDictDataService;
import com.gk.framework.service.intf.system.ISysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典表 服务实现类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */

@Service
@Slf4j
public class SysDictService extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Resource
    private ISysDictDataService sysDictDataService;
    @Resource
    private RedisManager redisManager;

    /**
     * 分页查询
     */
    @Override
    public IPage<SysDictPageResp> pageList(SysDictPageReq req) {
        return baseMapper.pageSysDict(req.createPage(), req);
    }

    /**
     * Excel导入
     */
    @Override
    public void importExcel(MultipartFile file) {
        log.error("File size: {}", file.getSize());
        List<String> dbDictCodeList = lambdaQuery().eq(SysDict::getEnabled, EnabledType.ENABLE.value)
                .select(SysDict::getDictCode)
                .list()
                .stream()
                .map(SysDict::getDictCode)
                .collect(Collectors.toList());
        SysDictExcelListener listener = new SysDictExcelListener(this, dbDictCodeList);
        ExcelUtils.read(file, SysDictExcelReq.class, listener);
    }

    /**
     * 全表导出
     */
    @Override
    public List<SysDictExportResp> exportList(SysDictExportReq req) {
        return baseMapper.exportSysDict(req);
    }

    /**
     * 新增
     */
    @Override
    public long add(SysDictAddReq req) {
        SysDict sysDict = req.toEntity();
        if (checkExist(req.getDictCode(), null)) {
            log.error("Add SysDict Error: Dict Name({}) or Dict Code({}) already exist", req.getDictName(), req.getDictCode());
            throw new SysException(SysStatus.DICT_CODE_EXIST);
        }
        if (!save(sysDict)) {
            log.error("Add SysDict Error: {}", JsonUtils.toJson(sysDict));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        return sysDict.getId();
    }

    /**
     * Excel 导入保存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveExcel(List<SysDictExcelReq> dictExcelReqList) {
        Map<String, List<SysDictExcelReq>> excelMap = dictExcelReqList.stream().collect(Collectors.groupingBy(SysDictExcelReq::getDictCode));
        for (Map.Entry<String, List<SysDictExcelReq>> entry : excelMap.entrySet()) {
            List<SysDictExcelReq> list = entry.getValue();
            if (CollUtil.isEmpty(list)) {
                continue;
            }
            // 取第一个转换成字典, 字典重复校验放在了listener中去做
            SysDictExcelReq firstDict = list.get(0);
            SysDict sysDict = firstDict.toDictEntity();
            if (!save(sysDict)) {
                log.error("Import SysDict Error: {}", JsonUtils.toJson(sysDict));
                throw new SysException(SysStatus.EXCEL_IMPORT_ERROR);
            }
            // 字典存储成功后, 再批量存储对应的字典项
            List<SysDictData> dictDataList = list.stream().map(SysDictExcelReq::toDictDataEntity).collect(Collectors.toList());
            sysDictDataService.importBatch(sysDict.getDictCode(), dictDataList);
        }
        // 单独做循环是因为存储数据库时有可能会报错, 报错后数据库可回滚, 但是缓存无法回滚
        // 所以先处理数据, 再处理缓存
        for (Map.Entry<String, List<SysDictExcelReq>> entry : excelMap.entrySet()) {
            List<SysDictExcelReq> list = entry.getValue();
            if (CollUtil.isEmpty(list)) {
                continue;
            }
            SysDictExcelReq firstDict = list.get(0);
            // 批量缓存字典项
            List<SysDictData> dictDataList = list.stream().map(SysDictExcelReq::toDictDataEntity).collect(Collectors.toList());
            DictCacheManager.importDictDataBatch(firstDict.getDictCode(), dictDataList);
        }
    }

    /**
     * 修改
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysDictEditReq req) {
        SysDict sysDict = req.toEntity();
        if (checkExist(req.getDictCode(), req.getId())) {
            log.error("Edit SysDict Error: Dict Name({}) or Dict Code({}) already exist", req.getDictName(), req.getDictCode());
            throw new SysException(SysStatus.DICT_CODE_EXIST);
        }
        SysDict dbDict = lambdaQuery().eq(SysDict::getId, req.getId())
                .eq(SysDict::getEnabled, EnabledType.ENABLE.value)
                .one();

        if (!updateById(sysDict)) {
            log.error("Edit SysDict Error: {}", JsonUtils.toJson(sysDict));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        if (!dbDict.getDictCode().equals(req.getDictCode())) {
            sysDictDataService.editDictCode(dbDict.getDictCode(), req.getDictCode());
        }
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<String> codeList) {
        boolean result = lambdaUpdate().in(SysDict::getDictCode, codeList)
                .set(SysDict::getEnabled, EnabledType.DISABLE.value)
                .update();
        if (!result) {
            log.error("Delete SysDict Error: {}", codeList);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        codeList.forEach(dictCode -> sysDictDataService.deleteAllByDictCode(dictCode));
    }

    /**
     * 单个查询
     */
    @Override
    public SysDictResp findOne(long id) {
        SysDict sysDict = lambdaQuery()
                .eq(SysDict::getId, id)
                .eq(SysDict::getEnabled, EnabledType.ENABLE.value)
                .one();
        if (sysDict == null) {
            return null;
        }
        return sysDict.toResp();
    }

    /**
     * 刷新所有字典缓存
     */
    @Override
    public void refreshCache() {
        List<String> dictKeyList = redisManager.matchScan(RedisConstant.DICT_DATA_KEY + CommonConstant.ASTERISK);
        redisManager.deleteBatch(dictKeyList);
        sysDictDataService.refreshLocalCache();
    }

    /**
     * 校验字典是否存在
     *
     * @param dictCode 字典编码
     * @param sysDictId 字典id
     * @return true:已存在 false:不存在
     */
    private boolean checkExist(String dictCode, Long sysDictId) {
        LambdaQueryChainWrapper<SysDict> qwDict = lambdaQuery()
                .eq(SysDict::getDictCode, dictCode)
                .eq(SysDict::getEnabled, EnabledType.ENABLE.value);
        // 修改时, 校验非当前字典是否重名或重复编码
        if (sysDictId != null) {
            qwDict.ne(SysDict::getId, sysDictId);
        }
        List<SysDict> dictList = qwDict.list();
        return CollUtil.isNotEmpty(dictList);
    }

}

