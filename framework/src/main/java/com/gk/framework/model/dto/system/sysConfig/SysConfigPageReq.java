package com.gk.framework.model.dto.system.sysConfig;

import com.gk.common.model.dto.PageDateReq;
import com.gk.framework.constant.DictConstant;
import com.gk.framework.validate.InDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 配置 分页请求类
 *
 * @author GuoYu
 * @since 2023-03-06 14:56:24
 */
@ApiModel(value = "配置分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfigPageReq extends PageDateReq {

    @ApiModelProperty(value = "配置名称", required = true, example = "配置名称")
    private String configName;

    @ApiModelProperty(value = "配置键", required = true, example = "配置键")
    private String configKey;

    @ApiModelProperty(value = "配置状态(1:正常, 0:停用)", required = true, example = "1")
    @InDict(DictConstant.CODE_DATA_STATUS)
    private String dataStatus;
}
