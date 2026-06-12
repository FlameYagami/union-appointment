package com.gk.server.model.dto.demo.serverDemo;

import com.gk.server.model.entity.demo.ServerDemo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 示例 新增请求类
 *
 * @author GuoYu
 * @since 2023-04-26 16:06:07
 */
@ApiModel(value = "示例新增请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerDemoAddReq extends ServerDemoBaseReq {

    @Override
    public ServerDemo toEntity() {
        return super.toEntity();
    }

}
