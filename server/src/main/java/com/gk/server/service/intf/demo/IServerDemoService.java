package com.gk.server.service.intf.demo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoAddReq;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoEditReq;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoExcelReq;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoExportReq;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoExportResp;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoPageReq;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoPageResp;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoResp;
import com.gk.server.model.entity.demo.ServerDemo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 示例表 服务接口类
 *
 * @author GuoYu
 * @since 2023-04-26 16:06:07
 */
public interface IServerDemoService extends IService<ServerDemo> {

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 分页列表
     */
    IPage<ServerDemoPageResp> pageList(ServerDemoPageReq req);

    /**
     * Excel导入
     *
     * @param file Excel文件
     */
    void importDemoExcel(MultipartFile file);

    /**
     * 全表导出
     *
     * @param req 查询条件
     * @return 全表数据
     */
    List<ServerDemoExportResp> exportList(ServerDemoExportReq req);

    /**
     * 新增
     *
     * @param req 请求类
     * @return 主键id
     */
    long add(ServerDemoAddReq req);

    /**
     * Excel 导入保存
     *
     * @param demoExcelReqList Excel导入请求类
     * @return 是否成功
     */
    boolean saveExcel(List<ServerDemoExcelReq> demoExcelReqList);

    /**
     * 修改
     *
     * @param req 请求类
     */
    void edit(ServerDemoEditReq req);

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
    ServerDemoResp findOne(Long id);

}
