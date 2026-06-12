package com.gk.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据过滤请求
 *
 *
 * @author Flame
 * @date 2023-02-02 17:22
 **/

@Data
public class DataScopeReq {

    /**
     * 部门id
     *
     * 针对数据过滤的机制做出完善措施, 在 {@link com.gk.common.enums.DataScopeType} 的定义为1, 2的情况下,
     * 数据直接由用户当前部门作为顶级机构查询, 但是如果切换到非顶级部门查询, 数据只会查询那一级部门.
     * 对于这种情况而言, 增设此字段用于在 DataScopeAspect 切面中替换deptId来完成非顶级部门及以下数据查询.
     */
    @ApiModelProperty(value = "部门id", example = "1000000000000000001")
    private Long deptId;

    /**
     * 是否本级部门数据
     *
     * 针对数据过滤的机制做出完善措施, 在 {@link com.gk.common.enums.DataScopeType} 的定义为1, 2的情况下,
     * 非顶级部门及以下数据查询在 {@link com.gk.common.model.dto.DataScopeReq} 中的 deptId 字段得到解决, 但数据不能只查询当前部门的数据.
     * 对于这种情况而言, 增设此字段用于在 DataScopeAspect 切面中判断是否替换级联查询Sql.
     */
    @ApiModelProperty(value = "是否本级部门数据", example = "false")
    private Boolean selfDept;

    /**
     * 数据过滤sql
     */
    @ApiModelProperty(hidden = true)
    private String dataScopeSql;

}
