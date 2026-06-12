package com.gk.app;

import com.gk.framework.model.dto.generate.GenColumnResp;
import com.gk.framework.model.dto.generate.GenTableReq;
import com.gk.framework.model.dto.generate.GenTableResp;
import com.gk.framework.model.dto.system.sysConfig.SysConfigExportReq;
import com.gk.framework.model.dto.system.sysConfig.SysConfigExportResp;
import com.gk.framework.model.dto.system.sysDictData.SysDictDataExportReq;
import com.gk.framework.model.dto.system.sysDictData.SysDictDataExportResp;
import com.gk.framework.service.intf.generate.IGenCodeService;
import com.gk.framework.service.intf.system.ISysConfigService;
import com.gk.framework.service.intf.system.ISysDictDataService;
import com.gk.framework.service.intf.system.ISysDictService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * SpringBoot业务 通用测试类
 *
 * @author GuoYu
 * @since 2023-02-15 14:16
 **/
@SpringBootTest(classes = {AppApplication.class})
class AppServiceTest {

    @Resource
    private ISysDictService sysDictService;

    @Resource
    private ISysDictDataService sysDictDataService;

    @Resource
    private ISysConfigService sysConfigService;

    @Resource
    private IGenCodeService genCodeService;

    @Test
    void testSysDict() {
        SysDictDataExportReq req = new SysDictDataExportReq();
        req.setDictCode("DataScope");
        List<SysDictDataExportResp> list = sysDictDataService.exportList(req);
        System.out.println(list.toString());
    }

    @Test
    void testRefreshDict() {
        sysDictService.refreshCache();
    }

    @Test
    void testSysConfig() {
        List<SysConfigExportResp> config = sysConfigService.exportList(new SysConfigExportReq());
        System.out.println(config);
    }

    @Test
    void testGenCode() {
        List<GenTableResp> tables = genCodeService.listTable(new GenTableReq());
        System.out.println(tables);

        List<GenColumnResp> columns = genCodeService.listTableColumn("sys_user");
        System.out.println(columns);
    }
}
