package com.gk.framework.model.dto.system.sysUser;

import com.gk.common.enums.YesOrNo;
import com.gk.framework.validate.InEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 黑名单状态 修改请求类
 *
 * @author Flame
 * @since 2024-04-07 9:30
 **/
@ApiModel(value = "黑名单状态修改请求")
@Data
public class BlacklistStatusChangeReq {

    @ApiModelProperty(value = "主键(userId)", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000002L, message = "id不合法") // 超管角色校验
    private long id;

    @ApiModelProperty(value = "黑名单状态(1:是, 0:否)", required = true, example = "1")
    @NotNull(message = "黑名单状态缺失")
    @InEnum(YesOrNo.class)
    private String blacklistStatus;

}
