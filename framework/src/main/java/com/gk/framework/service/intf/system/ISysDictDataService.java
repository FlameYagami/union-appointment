package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.framework.model.dto.system.base.DataStatusChangeReq;
import com.gk.framework.model.dto.system.sysDictData.*;
import com.gk.framework.model.entity.system.SysDictData;

import java.util.List;

/**
 * 字典数据表 服务接口类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
public interface ISysDictDataService extends IService<SysDictData> {

    /**
     * 刷新字典的本地缓存
     */
    void refreshLocalCache();

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 分页列表
     */
    IPage<SysDictDataPageResp> pageList(SysDictDataPageReq req);

    /**
     * 全表导出
     *
     * @param req 查询条件
     * @return 全表数据
     */
    List<SysDictDataExportResp> exportList(SysDictDataExportReq req);

    /**
     * 通过字典编码查询所有对应的字典项
     *
     * @param dictCode 字典编码
     * @return 对应字典编码的所有字典项
     */
    List<SysDictData> listByDictCode(String dictCode);

    /**
     * 新增
     *
     * @param req 请求类
     * @return 主键id
     */
    long add(SysDictDataAddReq req);

    /**
     * 批量新增导入
     *
     * @param dictCode 字典编码
     * @param dictDataList 字典项集合
     */
    void importBatch(String dictCode, List<SysDictData> dictDataList);

    /**
     * 修改
     *
     * @param req 请求类
     */
    void edit(SysDictDataEditReq req);

    /**
     * 字典编码修改, 字典项编码全部变更
     *
     * @param originDictCode 原字典编码
     * @param dictCode 修改的字典编码
     */
    void editDictCode(String originDictCode, String dictCode);

    /**
     * 删除
     *
     * @param idList 主键id集合
     * @param dictCode 字典编码
     */
    void delete(List<Long> idList, String dictCode);

    /**
     * 刷新单个字典的缓存
     *
     * @param dictCode 字典编码
     */
    void refreshCache(String dictCode);

    /**
     * 根据字典编码删除全部字典项
     *
     * @param dictCode 字典编码
     */
    void deleteAllByDictCode(String dictCode);

    /**
     * 单个查询
     *
     * @param id 主键id
     * @return 响应类
     */
    SysDictDataResp findOne(long id);

    void changeDataStatus(DataStatusChangeReq req);
}
