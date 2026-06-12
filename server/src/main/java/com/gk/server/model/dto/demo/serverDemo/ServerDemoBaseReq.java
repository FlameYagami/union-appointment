package com.gk.server.model.dto.demo.serverDemo;

import com.gk.server.model.entity.demo.ServerDemo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.gk.common.constant.DateConstant;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 示例 基础请求类
 *
 * @author GuoYu
 * @since 2023-04-26 16:06:07
 */
@Data
public class ServerDemoBaseReq {

    @ApiModelProperty(value = "示例名称", required = true, example = "示例名称")
    @NotBlank(message = "示例名称不能为空")
    private String demoName;

    @ApiModelProperty(value = "示例编码", required = true, example = "示例编码")
    @NotBlank(message = "示例编码不能为空")
    private String demoCode;

    @ApiModelProperty(value = "示例时间", required = true, example = "2000-01-31 23:59:59")
    @DateTimeFormat(pattern = DateConstant.DEFAULT_PATTERN)
    @NotNull(message = "示例时间不能为空")
    private Date demoTime;

    @ApiModelProperty(value = "数据状态", required = true, example = "数据状态")
    @NotBlank(message = "数据状态不能为空")
    private String dataStatus;

    @ApiModelProperty(value = "示例备注", required = true, example = "示例备注")
    @NotBlank(message = "示例备注不能为空")
    private String remark;

    /**
    * 转换成数据库操作类
    */
    protected ServerDemo toEntity() {
        return new ServerDemo()
                .setDemoName(demoName)
                .setDemoCode(demoCode)
                .setDemoTime(demoTime)
                .setDataStatus(dataStatus)
                .setRemark(remark);
    }

}
