package com.gk.server.mapper.demo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoExportReq;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoExportResp;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoPageReq;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoPageResp;
import com.gk.server.model.entity.demo.ServerDemo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 示例表 Mapper 接口
 *
 * @author GuoYu
 * @since 2023-04-26 16:06:07
 */

public interface ServerDemoMapper extends BaseMapper<ServerDemo> {

    /**
     * 分页查询
     */
    IPage<ServerDemoPageResp> pageServerDemo(IPage<?> page, @Param("req") ServerDemoPageReq req);

    /**
     * 全表导出
     */
    List<ServerDemoExportResp> exportServerDemo(@Param("req") ServerDemoExportReq req);
}
