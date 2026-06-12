package com.gk.server.model.dto.demo.serverDemo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 示例 导出请求类
 *
 * @author GuoYu
 * @since 2023-04-26 16:06:07
 */
@ApiModel(value = "示例导出请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerDemoExportReq extends ServerDemoPageReq {

}
