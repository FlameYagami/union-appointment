package com.gk.server.model.dto.activity;

import com.gk.server.model.entity.activity.Venue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * 场地 修改请求类
 *
 * @author Codex
 */
@ApiModel(value = "场地修改请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class VenueEditReq extends VenueBaseReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1, message = "id不合法")
    private long id;

    /**
     * 转换成数据库操作类
     */
    @Override
    public Venue toEntity() {
        return super.toEntity()
                .setId(id);
    }
}