package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk.framework.model.bo.system.sysDept.SysDeptTreeLabel;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeReq;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeResp;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeLabelQuery;
import com.gk.framework.model.entity.system.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门表 Mapper 接口
 *
 * @author Flame
 * @since 2022-12-28 17:10:44
 */

public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * 树形结构查询
     */
    List<SysDeptTreeResp> listSysDeptTree(@Param("req") SysDeptTreeReq req);

    /**
     * 获取部门树形结构列表(数据权限)
     */
    List<SysDeptTreeLabel> listSysDeptTreeLabel(@Param("query") SysDeptTreeLabelQuery req);

    /**
     * 获取部门树形结构列表
     */
    List<SysDeptTreeLabel> listSysDeptTreeLabelByDeptId(@Param("deptId") long deptId);

    /**
     * 获取父级部门的子部门ids
     */
    List<Long> getChildDeptIds(@Param("deptId") long deptId);

    /**
     * 获取生效中的父级部门的子部门ids
     */
    List<Long> getEnableChildDeptIds(@Param("deptId") long deptId);

    /**
     * 获取父级部门的子部门集合
     */
    List<SysDept> listChildDept(@Param("deptId") long deptId);

    /**
     * 获取生效中的父级部门的子部门集合
     */
    List<SysDept> listEnableChildDept(@Param("deptId") long deptId);

    /**
     * 获取部门的所有父级部门id集合
     */
    List<Long> getParentDeptIds(@Param("deptId") long deptId);

}
