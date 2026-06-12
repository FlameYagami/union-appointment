package com.gk.framework.model.dto.system.sysExceptionLog;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 异常日志 导出响应类
 *
 * @author GuoYu
 * @since 2024-05-15 15:16:18
 */
@Data
@Accessors(chain = true)
@HeadRowHeight(20)
public class SysExceptionLogExportResp {

    @ExcelProperty("账号")
    private String username;

    @ExcelProperty("异常类型(1:暴力登录, 2:异地登录, 3:账号锁定, 4:密码变更)")
    private String exceptionType;

    @ExcelProperty("ip地址")
    private String ip;

    @ExcelProperty("ip归属地")
    private String location;

    @ExcelProperty("浏览器")
    private String browser;

    @ExcelProperty("操作系统")
    private String os;
}
