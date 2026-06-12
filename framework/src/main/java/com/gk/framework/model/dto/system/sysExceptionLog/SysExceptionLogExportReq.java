package com.gk.framework.model.dto.system.sysExceptionLog;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 异常日志 导出请求类
 *
 * @author GuoYu
 * @since 2024-05-15 15:16:18
 */
@ApiModel(value = "异常日志导出请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysExceptionLogExportReq extends SysExceptionLogPageReq {

}
