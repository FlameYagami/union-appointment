package com.gk.framework.mapper.generate;

import com.gk.framework.model.dto.generate.GenColumnResp;
import com.gk.framework.model.dto.generate.GenTableReq;
import com.gk.framework.model.dto.generate.GenTableResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 前端代码生成 Mapper层
 *
 * @author GuoYu
 * @since 2024-03-15 16:54
 **/
@Mapper
public interface GenCodeMapper {

    /**
     * 查询所有数据表
     */
    List<GenTableResp> listTable(@Param("req") GenTableReq req);

    /**
     * 查询对应表的所有字段
     *
     * @param tableName 表名
     */
    List<GenColumnResp> listTableColumn(@Param("tableName") String tableName);
}
