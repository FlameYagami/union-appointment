package com.gk.framework.model.dto.system.sysDict;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典 导出请求类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@ApiModel(value = "字典导出请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictExportReq extends SysDictPageReq {

}
