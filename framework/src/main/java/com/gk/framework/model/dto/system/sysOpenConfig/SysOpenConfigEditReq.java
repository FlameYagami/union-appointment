package com.gk.framework.model.dto.system.sysOpenConfig;

import com.gk.framework.model.entity.system.SysOpenConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * 第三方对接配置 修改请求类
 *
 * @author Flame
 * @since 2023-03-13 16:26:48
 */
@ApiModel(value = "第三方对接配置修改请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOpenConfigEditReq extends SysOpenConfigBaseReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long id;

    @Override
    public SysOpenConfig toEntity() {
        return super.toEntity()
                .setId(id);
    }

}
