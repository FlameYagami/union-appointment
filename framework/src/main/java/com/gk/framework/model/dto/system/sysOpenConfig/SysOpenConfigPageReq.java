package com.gk.framework.model.dto.system.sysOpenConfig;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 第三方对接配置 分页请求类
 *
 * @author Flame
 * @since 2023-03-13 16:26:48
 */
@ApiModel(value = "第三方对接配置分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOpenConfigPageReq extends PageDateReq {

    @ApiModelProperty(value = "秘钥状态(1:正常, 0:停用)", required = true, example = "1")
    private String dataStatus;

    @ApiModelProperty(value = "对接厂商名称", required = true, example = "对接厂商名称")
    private String companyName;

}
