package com.gk.server.model.dto.demo.serverDemo;

import com.gk.server.model.entity.demo.ServerDemo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * 示例 修改请求类
 *
 * @author GuoYu
 * @since 2023-04-26 16:06:07
 */
@ApiModel(value = "示例修改请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerDemoEditReq extends ServerDemoBaseReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long id;

    @Override
    public ServerDemo toEntity() {
        return super.toEntity()
                .setId(id);
    }

}
