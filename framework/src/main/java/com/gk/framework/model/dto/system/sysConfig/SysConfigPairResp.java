package com.gk.framework.model.dto.system.sysConfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 配置 键值对响应类
 *
 * @author GuoYu
 * @since 2023-03-06 14:56:24
 */
@ApiModel(value = "配置响应")
@Data
@AllArgsConstructor
public class SysConfigPairResp {

    @ApiModelProperty(value = "配置键", example = "key")
    private String configKey;

    @ApiModelProperty(value = "配置值", example = "value")
    private String configValue;

}
