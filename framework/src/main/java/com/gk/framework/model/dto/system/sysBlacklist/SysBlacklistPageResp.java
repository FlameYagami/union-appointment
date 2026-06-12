package com.gk.framework.model.dto.system.sysBlacklist;

import cn.hutool.core.util.DesensitizedUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 黑名单 分页响应类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@ApiModel(value = "黑名单分页响应")
@Data
@Accessors(chain = true)
public class SysBlacklistPageResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "姓名", example = "管理员")
    private String nickname;

    @ApiModelProperty(value = "手机号(脱敏)", example = "15800000000")
    private String telephone;

    public void handleData() {
        telephone = DesensitizedUtil.mobilePhone(telephone);
    }

}
