package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.framework.model.dto.system.sysConfig.*;
import com.gk.framework.model.entity.system.SysConfig;

import java.util.*;

/**
 * 配置表 服务接口类
 *
 * @author GuoYu
 * @since 2023-03-06 14:56:24
 */
public interface ISysConfigService extends IService<SysConfig> {

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 分页列表
     */
    IPage<SysConfigPageResp> pageList(SysConfigPageReq req);

    /**
     * 全表导出
     *
     * @param req 查询条件
     * @return 全表数据
     */
    List<SysConfigExportResp> exportList(SysConfigExportReq req);

    /**
     * 新增
     *
     * @param req 请求类
     * @return 主键id
     */
    long add(SysConfigAddReq req);

    /**
     * 修改
     *
     * @param req 请求类
     */
    void edit(SysConfigEditReq req);

    /**
     * 删除
     *
     * @param idList 主键id集合
     */
    void delete(List<Long> idList);

    /**
     * 单个查询
     *
     * @param id 主键id
     * @return 响应类
     */
    SysConfigResp findOne(long id);

    /**
     * 通过配置键来查询
     *
     * @param configKey 参数键
     * @return 数据库类
     */
    SysConfig findByKey(String configKey);

    /**
     * 通过配置键来查询
     *
     * @param configKeys 参数键
     * @return 数据库类
     */
    List<SysConfigPairResp> listConfigPair(List<String> configKeys);

    /**
     * 批量清除Redis缓存
     *
     * @param idList 配置id集合 如果传入指定的id集合, 则清除指定的缓存, 如果传null, 则清除所有缓存
     */
    void refreshCache(List<Long> idList);
}
