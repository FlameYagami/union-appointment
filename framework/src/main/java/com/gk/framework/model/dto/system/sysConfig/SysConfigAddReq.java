package com.gk.framework.model.dto.system.sysConfig;

import com.gk.framework.model.entity.system.SysConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 配置 新增请求类
 *
 * @author GuoYu
 * @since 2023-03-06 14:56:24
 */
@ApiModel(value = "配置新增请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfigAddReq extends SysConfigBaseReq {

    @Override
    public SysConfig toEntity() {
        return super.toEntity();
    }

}
