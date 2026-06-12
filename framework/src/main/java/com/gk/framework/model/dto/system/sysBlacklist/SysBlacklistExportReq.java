package com.gk.framework.model.dto.system.sysBlacklist;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 黑名单 导出请求类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@ApiModel(value = "黑名单导出请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysBlacklistExportReq extends SysBlacklistPageReq {

}
