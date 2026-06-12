package com.gk.framework.model.dto.system.sysConfig;

import com.gk.framework.model.entity.system.SysConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * 配置 修改请求类
 *
 * @author GuoYu
 * @since 2023-03-06 14:56:24
 */
@ApiModel(value = "配置修改请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfigEditReq extends SysConfigBaseReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long id;

    @Override
    public SysConfig toEntity() {
        return super.toEntity()
                .setId(id);
    }

}
