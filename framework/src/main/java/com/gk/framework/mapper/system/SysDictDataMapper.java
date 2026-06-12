package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk.framework.model.dto.system.sysDictData.SysDictDataExportReq;
import com.gk.framework.model.dto.system.sysDictData.SysDictDataExportResp;
import com.gk.framework.model.dto.system.sysDictData.SysDictDataPageReq;
import com.gk.framework.model.dto.system.sysDictData.SysDictDataPageResp;
import com.gk.framework.model.entity.system.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典数据表 Mapper 接口
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */

public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    /**
     * 分页查询
     */
    IPage<SysDictDataPageResp> pageSysDictData(IPage<?> page, @Param("req") SysDictDataPageReq req);

    /**
     * 全表导出
     */
    List<SysDictDataExportResp> exportSysDictData(@Param("req") SysDictDataExportReq req);

    /**
     * 根据主键id集合查找字典值的集合
     *
     * @param idList 主键id集合
     * @return 字典值集合
     */
    List<String> listDictValueById(@Param("idList") List<Long> idList);
}
