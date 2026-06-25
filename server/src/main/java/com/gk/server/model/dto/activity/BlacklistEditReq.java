package com.gk.server.model.dto.activity;

import com.gk.server.model.entity.activity.AppointmentBlacklist;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * 黑名单 修改请求类
 *
 * @author Codex
 */
@ApiModel(value = "黑名单修改请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class BlacklistEditReq extends BlacklistBaseReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1, message = "id不合法")
    private long id;

    @ApiModelProperty(value = "违约次数", example = "1")
    private int violateCount;

    @ApiModelProperty(value = "封禁状态：0-解封，1-封禁", example = "1")
    private String status;

    /**
     * 转换成数据库操作类
     */
    public AppointmentBlacklist toEntity() {
        return super.toEntity()
                .setId(id)
                .setViolateCount(violateCount)
                .setStatus(status);
    }
}