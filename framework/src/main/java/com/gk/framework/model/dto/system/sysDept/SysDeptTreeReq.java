package com.gk.framework.model.dto.system.sysDept;

import com.gk.common.model.dto.DataScopeReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部门 树形结构请求类
 *
 * @author Flame
 * @since 2022-12-28 17:10:44
 */
@ApiModel(value = "部门树形结构请求")
@Data
public class SysDeptTreeReq extends DataScopeReq {

    @ApiModelProperty(value = "部门名称", example = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "部门状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

}
