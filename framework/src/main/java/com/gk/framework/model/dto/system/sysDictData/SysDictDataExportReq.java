package com.gk.framework.model.dto.system.sysDictData;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据 导出请求类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@ApiModel(value = "字典数据导出请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictDataExportReq extends SysDictDataPageReq {

}
