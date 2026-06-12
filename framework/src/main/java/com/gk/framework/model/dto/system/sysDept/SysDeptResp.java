package com.gk.framework.model.dto.system.sysDept;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 部门 响应类
 *
 * @author Flame
 * @since 2022-12-28 17:10:44
 */
@Data
@Accessors(chain = true)
public class SysDeptResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "父级id", example = "1000000000000000001")
    private long parentId;

    @ApiModelProperty(value = "部门名称", example = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "部门编码", example = "部门编码")
    private String deptCode;

    @ApiModelProperty(value = "部门层级", example = "100")
    private int deptLevel;

    @ApiModelProperty(value = "部门类型", example = "部门类型")
    private String deptType;

    @ApiModelProperty(value = "部门状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

    @ApiModelProperty(value = "部门排序", example = "100")
    private int deptOrder;

}
