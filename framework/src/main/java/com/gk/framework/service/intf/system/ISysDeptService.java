package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.common.model.dto.LabelResp;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.framework.model.dto.system.sysDept.SysDeptAddReq;
import com.gk.framework.model.dto.system.sysDept.SysDeptEditReq;
import com.gk.framework.model.dto.system.sysDept.SysDeptResp;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeLabelQuery;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeReq;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeResp;
import com.gk.framework.model.entity.system.SysDept;

import java.util.List;

/**
 * 部门表 服务接口类
 *
 * @author Flame
 * @since 2022-12-28 17:10:44
 */
public interface ISysDeptService extends IService<SysDept> {

    /**
     * 树形结构查询
     */
    List<SysDeptTreeResp> listTree(SysDeptTreeReq req);

    /**
     * 新增
     *
     * @param req 请求类
     * @return 主键id
     */
    long add(SysDeptAddReq req);

    /**
     * 修改
     *
     * @param req 请求类
     */
    void edit(SysDeptEditReq req);

    /**
     * 删除
     *
     * @param id 主键id
     */
    void delete(long id);

    /**
     * 单个查询
     *
     * @param id 主键id
     * @return 响应类
     */
    SysDeptResp findOne(long id);

    List<TreeLabelResp> listTreeLabel(SysDeptTreeLabelQuery query);

    List<TreeLabelResp> listTreeLabelByDeptId(long deptId);

    List<Long> getChildDeptIds(long deptId, boolean includeDeptId);

    List<Long> getEnableChildDeptIds(long deptId, boolean includeDeptId);

    List<SysDept> listChildDept(long deptId, boolean includeDeptId);

    List<SysDept> listEnableChildDept(long deptId, boolean includeDeptId);

    List<LabelResp> listDeptLabel(List<Long> deptIds);

    List<Long> getParentDeptIds(long deptId, boolean includeDeptId);
}
