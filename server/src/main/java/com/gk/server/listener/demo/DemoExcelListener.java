package com.gk.server.listener.demo;

import cn.hutool.core.util.StrUtil;
import com.gk.common.constant.ExcelErrorConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.listener.base.BaseReadListener;
import com.gk.common.model.exception.SysException;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoExcelReq;
import com.gk.server.service.intf.demo.IServerDemoService;

import java.util.List;

/**
 * 示例表 Excel导入监听器
 *
 * @author GuoYu
 * @since 2023-04-26 15:43
 **/
public class DemoExcelListener extends BaseReadListener<ServerDemoExcelReq> {
    /** 业务处理实现类 */
    private final IServerDemoService serverDemoService;

    /**
     * 初始化函数把业务实现类传入
     * 如果不想在complete方法中去保存数据, 可以不用把service传入
     */
    public DemoExcelListener(IServerDemoService serverDemoService) {
        // super必须写, 需要在BaseReadListener中使用, 传入的是ExcelReq请求类的类型
        super(ServerDemoExcelReq.class);
        this.serverDemoService = serverDemoService;
    }

    @Override
    public void validEachRow(ServerDemoExcelReq req) {
        // 必填项校验或其他业务数值校验
        if (StrUtil.isEmpty(req.getDemoName())) {
            // 错误信息用Constant类进行管理, 不要随意写在代码内
            // toError模型转换方法写在DTO层, 把Req类转为Error类, 添加到errors错误信息集合中
            // 如果errors不为空, 则会通过抛异常走全局捕获的方式返回给前端错误信息列表, 可以让前端展示
            errors.add(req.toError(ExcelErrorConstant.DEMO_NAME_EMPTY_ERROR));
        }
    }

    @Override
    public String validUnique(ServerDemoExcelReq req) {
        // 判断重复数据的规则
        return req.getDemoName() + req.getDemoCode();
    }

    @Override
    public void complete(List<ServerDemoExcelReq> allList) {
        // 调用业务实现类中保存Excel数据入数据库的方法
        if (!serverDemoService.saveExcel(allList)) {
            // 数据库保存失败, 抛出业务异常, 全局异常捕获类会处理成异常请求响应返回接口
            throw new SysException(SysStatus.EXCEL_IMPORT_ERROR);
        }
    }
}
