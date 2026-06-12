package com.gk.framework.model.dto.system.sysConfig;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 配置 分页响应类
 *
 * @author GuoYu
 * @since 2023-03-06 14:56:24
 */
@ApiModel(value = "配置分页响应")
@Data
@Accessors(chain = true)
public class SysConfigPageResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "配置名称", example = "配置名称")
    private String configName;

    @ApiModelProperty(value = "配置键", example = "配置键")
    private String configKey;

    @ApiModelProperty(value = "配置值", example = "配置值")
    private String configValue;

    @ApiModelProperty(value = "是否为系统数据(1:是, 0:否, 默认:0)", example = "0")
    private String systemData;

    @ApiModelProperty(value = "配置状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

    @ApiModelProperty(value = "备注", example = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间", example = "2023-02-01 10:10:10")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;
}
