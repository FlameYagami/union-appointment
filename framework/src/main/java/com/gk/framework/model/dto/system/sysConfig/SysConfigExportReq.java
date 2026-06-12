package com.gk.framework.model.dto.system.sysConfig;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 配置 导出请求类
 *
 * @author GuoYu
 * @since 2023-03-06 14:56:24
 */
@ApiModel(value = "配置导出请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfigExportReq extends SysConfigPageReq {

}
