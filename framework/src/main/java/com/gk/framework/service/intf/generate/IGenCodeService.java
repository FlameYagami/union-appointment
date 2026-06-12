package com.gk.framework.service.intf.generate;

import com.gk.framework.model.dto.generate.GenColumnResp;
import com.gk.framework.model.dto.generate.GenReq;
import com.gk.framework.model.dto.generate.GenTableReq;
import com.gk.framework.model.dto.generate.GenTableResp;

import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * 前端代码生成 业务接口类
 *
 * @author GuoYu
 * @since 2024-03-15 16:50
 **/
public interface IGenCodeService {
    /**
     * 查询所有数据表
     *
     * @param req 请求类
     */
    List<GenTableResp> listTable(GenTableReq req);

    /**
     * 查询对应表的所有字段
     *
     * @param tableName 表名
     */
    List<GenColumnResp> listTableColumn(String tableName);

    /**
     * 生成前端代码 zip压缩后下载
     *
     * @param req 请求类
     * @param zipOutputStream zip输出流
     */
    void generatorCode(GenReq req, ZipOutputStream zipOutputStream);
}
