package com.gk.server.model.dto.demo.serverDemo;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.gk.common.constant.DateConstant;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 示例 分页请求类
 *
 * @author GuoYu
 * @since 2023-04-26 16:06:07
 */
@ApiModel(value = "示例分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerDemoPageReq extends PageDateReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "示例名称", required = true, example = "示例名称")
    private String demoName;

    @ApiModelProperty(value = "示例编码", required = true, example = "示例编码")
    private String demoCode;

    @ApiModelProperty(value = "示例时间", required = true, example = "2000-01-31 23:59:59")
    @DateTimeFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date demoTime;

    @ApiModelProperty(value = "数据状态", required = true, example = "数据状态")
    private String dataStatus;

    @ApiModelProperty(value = "示例备注", required = true, example = "示例备注")
    private String remark;
}
