package com.gk.server.model.dto.activity;

import com.gk.server.model.entity.activity.Schedule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * 排期 修改请求类
 *
 * @author Codex
 */
@ApiModel(value = "排期修改请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleEditReq extends ScheduleBaseReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1, message = "id不合法")
    private long id;

    /**
     * 转换成数据库操作类
     */
    @Override
    public Schedule toEntity() {
        return super.toEntity()
                .setId(id);
    }
}