package com.gk.framework.model.dto.system.sysDept;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import com.gk.common.model.base.BaseTreeResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 部门 树形结构响应类
 *
 * @author Flame
 * @since 2022-12-28 17:10:44
 */
@ApiModel(value = "部门树形结构响应")
@Data
public class SysDeptTreeResp extends BaseTreeResp<SysDeptTreeResp> {

    @ApiModelProperty(value = "部门名称", example = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "部门编码", example = "部门编码")
    private String deptCode;

    @ApiModelProperty(value = "部门状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

    @ApiModelProperty(value = "部门排序", example = "100")
    private int deptOrder;

    @ApiModelProperty(value = "创建时间", example = "2023-02-01 10:10:10")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;

}
