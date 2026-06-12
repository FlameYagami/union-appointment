package com.gk.framework.model.dto.system.sysBlacklist;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户 分页请求类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@ApiModel(value = "用户分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysBlacklistPageReq extends PageDateReq {

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "手机号", example = "15800000000")
    private String telephone;

    @ApiModelProperty(value = "姓名", example = "管理员")
    private String nickname;

}
