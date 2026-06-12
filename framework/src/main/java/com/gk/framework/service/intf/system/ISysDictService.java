package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.framework.model.dto.system.sysDict.SysDictAddReq;
import com.gk.framework.model.dto.system.sysDict.SysDictEditReq;
import com.gk.framework.model.dto.system.sysDict.SysDictExcelReq;
import com.gk.framework.model.dto.system.sysDict.SysDictExportReq;
import com.gk.framework.model.dto.system.sysDict.SysDictExportResp;
import com.gk.framework.model.dto.system.sysDict.SysDictPageReq;
import com.gk.framework.model.dto.system.sysDict.SysDictPageResp;
import com.gk.framework.model.dto.system.sysDict.SysDictResp;
import com.gk.framework.model.entity.system.SysDict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 字典表 服务接口类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
public interface ISysDictService extends IService<SysDict> {

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 分页列表
     */
    IPage<SysDictPageResp> pageList(SysDictPageReq req);

    /**
     * Excel导入
     *
     * @param file Excel文件
     */
    void importExcel(MultipartFile file);

    /**
     * 全表导出
     *
     * @param req 查询条件
     * @return 全表数据
     */
    List<SysDictExportResp> exportList(SysDictExportReq req);

    /**
     * 新增
     *
     * @param req 请求类
     * @return 主键id
     */
    long add(SysDictAddReq req);

    /**
     * Excel 导入保存
     *
     * @param dictExcelReqList Excel导入请求类
     */
    @Transactional(rollbackFor = Exception.class)
    void saveExcel(List<SysDictExcelReq> dictExcelReqList);

    /**
     * 修改
     *
     * @param req 请求类
     */
    void edit(SysDictEditReq req);

    /**
     * 删除
     *
     * @param codeList 字典编码集合
     */
    void delete(List<String> codeList);

    /**
     * 单个查询
     *
     * @param id 主键id
     * @return 响应类
     */
    SysDictResp findOne(long id);

    /**
     * 刷新所有字典缓存
     */
    void refreshCache();
}
