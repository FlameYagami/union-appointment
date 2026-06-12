package com.gk.server.model.dto.system.sysUser;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户 导出请求类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@ApiModel(value = "用户导出请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserExportReq extends SysUserPageReq {

}
