package com.gk.server.service.impl.demo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.ExcelUtils;
import com.gk.common.utils.JsonUtils;
import com.gk.server.listener.demo.DemoExcelListener;
import com.gk.server.mapper.demo.ServerDemoMapper;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoAddReq;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoEditReq;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoExcelReq;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoExportReq;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoExportResp;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoPageReq;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoPageResp;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoResp;
import com.gk.server.model.entity.demo.ServerDemo;
import com.gk.server.service.intf.demo.IServerDemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 示例表 服务实现类
 *
 * @author GuoYu
 * @since 2023-04-26 16:06:07
 */

@Service
@Slf4j
public class ServerDemoService extends ServiceImpl<ServerDemoMapper, ServerDemo> implements IServerDemoService {

    /**
     * 分页查询
     */
    @Override
    public IPage<ServerDemoPageResp> pageList(ServerDemoPageReq req) {
        return baseMapper.pageServerDemo(req.createPage(), req);
    }

    /**
     * Excel导入
     */
    @Override
    public void importDemoExcel(MultipartFile file) {
        DemoExcelListener listener = new DemoExcelListener(this);
        // 导入异常都在工具类内部处理了, 这里无需再做异常处理
        ExcelUtils.read(file, ServerDemoExcelReq.class, listener);
    }

    /**
     * 全表导出
     */
    @Override
    public List<ServerDemoExportResp> exportList(ServerDemoExportReq req) {
        return baseMapper.exportServerDemo(req);
    }

    /**
     * 新增
     */
    @Override
    public long add(ServerDemoAddReq req) {
        ServerDemo serverDemo = req.toEntity();
        if (!save(serverDemo)) {
            log.error("Add ServerDemo Error: {}", JsonUtils.toJson(serverDemo));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        return serverDemo.getId();
    }

    /**
     * Excel 导入保存
     */
    @Override
    public boolean saveExcel(List<ServerDemoExcelReq> demoExcelReqList) {
        // 如果需要校验已存在项, 请记得在此校验 或 在listener中校验
        List<ServerDemo> demoList = demoExcelReqList.stream().map(ServerDemoExcelReq::toEntity).collect(Collectors.toList());
        return saveBatch(demoList, demoList.size());
    }

    /**
     * 修改
     */
    @Override
    public void edit(ServerDemoEditReq req) {
        ServerDemo serverDemo = req.toEntity();
        if (!updateById(serverDemo)) {
            log.error("Edit ServerDemo Error: {}", JsonUtils.toJson(serverDemo));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 删除
     */
    @Override
    public void delete(List<Long> idList) {
        boolean result = lambdaUpdate()
                .in(ServerDemo::getId, idList)
                .set(ServerDemo::getEnabled, EnabledType.DISABLE.value)
                .update();
        if (!result) {
            log.error("Delete ServerDemo Error: {}", idList);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 单个查询
     */
    @Override
    public ServerDemoResp findOne(Long id) {
        ServerDemo serverDemo = lambdaQuery()
                .eq(ServerDemo::getId, id)
                .eq(ServerDemo::getEnabled, EnabledType.ENABLE.value)
                .one();
        if (serverDemo == null) {
            return null;
        }
        return serverDemo.toResp();
    }

}

