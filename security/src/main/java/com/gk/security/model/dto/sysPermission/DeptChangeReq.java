package com.gk.security.model.dto.sysPermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 部门切换 请求类
 *
 * @author Flame
 * @date 2023-01-31 16:08
 **/
@ApiModel(value = "部门切换请求")
@Data
public class DeptChangeReq {

    @ApiModelProperty(value = "部门id", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long deptId;

}
