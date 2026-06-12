package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk.framework.model.dto.system.sysDict.SysDictExportReq;
import com.gk.framework.model.dto.system.sysDict.SysDictExportResp;
import com.gk.framework.model.dto.system.sysDict.SysDictPageReq;
import com.gk.framework.model.dto.system.sysDict.SysDictPageResp;
import com.gk.framework.model.entity.system.SysDict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典表 Mapper 接口
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */

public interface SysDictMapper extends BaseMapper<SysDict> {

    /**
     * 分页查询
     */
    IPage<SysDictPageResp> pageSysDict(IPage<?> page, @Param("req") SysDictPageReq req);

    /**
     * 全表导出
     */
    List<SysDictExportResp> exportSysDict(@Param("req") SysDictExportReq req);
}
