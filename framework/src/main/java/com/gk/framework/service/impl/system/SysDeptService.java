package com.gk.framework.service.impl.system;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.DataStatus;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.model.dto.LabelResp;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.common.utils.JsonUtils;
import com.gk.common.utils.TreeUtils;
import com.gk.framework.annotation.DataScope;
import com.gk.framework.mapper.system.SysDeptMapper;
import com.gk.framework.model.bo.system.sysDept.SysDeptTreeLabel;
import com.gk.framework.model.dto.system.sysDept.SysDeptAddReq;
import com.gk.framework.model.dto.system.sysDept.SysDeptEditReq;
import com.gk.framework.model.dto.system.sysDept.SysDeptResp;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeLabelQuery;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeReq;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeResp;
import com.gk.framework.model.entity.system.SysDept;
import com.gk.framework.service.intf.system.ISysDeptService;
import com.gk.framework.service.intf.system.ISysUserDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门表 服务实现类
 *
 * @author Flame
 * @since 2022-12-28 17:10:44
 */

@Service
@Slf4j
public class SysDeptService extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Resource
    private ISysUserDeptService sysUserDeptService;

    /**
     * 树形结构查询
     */
    @DataScope(bizTableAlias = "sd", deptIdAlias = "id")
    @Override
    public List<SysDeptTreeResp> listTree(SysDeptTreeReq req) {
        List<SysDeptTreeResp> resp = baseMapper.listSysDeptTree(req);
        return TreeUtils.buildTree(resp);
    }

    /**
     * 新增
     */
    @Override
    public long add(SysDeptAddReq req) {
        // 校验父级部门id合法性
        checkParentIdLegal(null, req.getParentId());
        // 校验部门编码合法性
        checkDeptCodeLegal(null, req.getDeptCode());

        // 计算当前部门的级别
        SysDept parentDept = getById(req.getParentId());
        int deptLevel = parentDept.getDeptLevel() + 1;
        SysDept sysDept = req.toEntity(deptLevel);
        if (!save(sysDept)) {
            log.error("Add SysDept Error: {}", JsonUtils.toJson(sysDept));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        return sysDept.getId();
    }

    /**
     * 修改
     */
    @Transactional
    @Override
    public void edit(SysDeptEditReq req) {
        // 顶级部门优先修改判定(只更新部门名称和部门编码)
        if (CommonConstant.TOP_ID == req.getId()) {
            boolean result = lambdaUpdate()
                    .set(StrUtil.isNotEmpty(req.getDeptName()), SysDept::getDeptName, req.getDeptName())
                    .set(StrUtil.isNotEmpty(req.getDeptCode()), SysDept::getDeptCode, req.getDeptCode())
                    .eq(SysDept::getId, CommonConstant.TOP_ID)
                    .update();
            if (!result) {
                log.error("Edit RootSysDept Error: deptName({}), deptCode({})", req.getDeptName(), req.getDeptCode());
                throw new SysException(SysStatus.OPERATE_FAILED);
            }
            return;
        }

        // 校验父级部门id合法性
        checkParentIdLegal(req.getId(), req.getParentId());
        // 校验部门编码合法性
        checkDeptCodeLegal(req.getId(), req.getDeptCode());

        // 计算当前部门的级别
        SysDept parentDept = getById(req.getParentId());
        int deptLevel = parentDept.getDeptLevel() + 1;

        // 批量修改子部门
        SysDept dbEntity = getById(req.getId());
        if (!updateChild(dbEntity, req, deptLevel)) {
            log.error("Edit SysDept Error in update child dept: {}", JsonUtils.toJson(req));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 修改当前部门
        SysDept sysDept = req.toEntity(deptLevel);
        if (!updateById(sysDept)) {
            log.error("Edit SysDept Error: {}", JsonUtils.toJson(sysDept));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    private boolean updateChild(SysDept dbEntity, SysDeptEditReq req, int deptLevel) {
        // 查询子部门
        List<SysDept> childDepts = listChildDept(req.getId(), false);
        if (childDepts.isEmpty()) {
            return true;
        }

        // 数据库中的状态与修改的状态一致则不需要修改
        String dataStatus = null;
        if (!dbEntity.getDataStatus().equals(req.getDataStatus())) {
            dataStatus = req.getDataStatus();
        }
        String finalDataStatus = dataStatus;

        // 部门等级偏差值
        int deptLevelDeviation = 0;
        if (dbEntity.getDeptLevel() != deptLevel) {
            deptLevelDeviation = dbEntity.getDeptLevel() - deptLevel; // 偏差值大于0, 说明部门向上级调整, 反之则向下条调整
        }
        int finalDeptLevelDeviation = deptLevelDeviation;

        // 调整需要修改的属性
        childDepts.forEach(dept -> {
            if (0 != finalDeptLevelDeviation) {
                dept.setDeptLevel(deptLevel - finalDeptLevelDeviation); // 自适应修改当前部门的所有子部门级别
            }
            if (null != finalDataStatus) {
                dept.setDataStatus(finalDataStatus); // 自适应修改当前部门的所有子部门使用状态
            }
        });
        // 保存数据
        return updateBatchById(childDepts);
    }

    /**
     * 删除
     */
    @Override
    public void delete(long id) {
        // 顶级部门校验
        if (CommonConstant.TOP_ID == id) {
            throw new SysException(SysStatus.TOP_DEPT_CAN_NOT_OPERATE);
        }

        // 子部门判定
        boolean existChild = lambdaQuery()
                .eq(SysDept::getEnabled, EnabledType.ENABLE.value)
                .eq(SysDept::getParentId, id)
                .exists();
        if (existChild) {
            throw new SysException(SysStatus.DEPT_EXITS_CHILD);
        }

        // 用户使用判定
        if (sysUserDeptService.hasUseDept(id)) {
            throw new SysException(SysStatus.DEPT_EXISTS_USER);
        }

        // 部门删除
        boolean result = lambdaUpdate()
                .set(SysDept::getEnabled, EnabledType.DISABLE.value)
                .eq(SysDept::getId, id)
                .update();
        if (!result) {
            log.error("Delete SysDept Error: {}", id);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 单个查询
     */
    @Override
    public SysDeptResp findOne(long id) {
        SysDept sysDept = lambdaQuery()
                .eq(SysDept::getId, id)
                .eq(SysDept::getEnabled, EnabledType.ENABLE.value)
                .one();
        if (sysDept == null) {
            return null;
        }
        return sysDept.toResp();
    }

    /**
     * 获取部门树形结构列表(数据权限)
     */
    @DataScope(bizTableAlias = "sd", deptIdAlias = "id")
    @Override
    public List<TreeLabelResp> listTreeLabel(SysDeptTreeLabelQuery req) {
        List<TreeLabelResp> resp = baseMapper.listSysDeptTreeLabel(req).stream()
                .map(SysDeptTreeLabel::toTreeLabelResp)
                .collect(Collectors.toList());
        return TreeUtils.buildTree(resp);
    }

    /**
     * 获取部门树形结构列表
     */
    @Override
    public List<TreeLabelResp> listTreeLabelByDeptId(long deptId) {
        List<TreeLabelResp> resp = baseMapper.listSysDeptTreeLabelByDeptId(deptId).stream()
                .map(SysDeptTreeLabel::toTreeLabelResp)
                .collect(Collectors.toList());
        return TreeUtils.buildTree(resp);
    }

    /**
     * 获取父级部门的子部门ids
     */
    @Override
    public List<Long> getChildDeptIds(long deptId, boolean includeDeptId) {
        List<Long> ids = baseMapper.getChildDeptIds(deptId);
        if (includeDeptId) {
            ids.add(0, deptId);
        }
        return ids;
    }

    /**
     * 获取生效中的父级部门的子部门ids
     */
    @Override
    public List<Long> getEnableChildDeptIds(long deptId, boolean includeDeptId) {
        List<Long> ids = baseMapper.getEnableChildDeptIds(deptId);
        if (includeDeptId) {
            ids.add(0, deptId);
        }
        return ids;
    }

    /**
     * 获取父级部门的子部门
     */
    @Override
    public List<SysDept> listChildDept(long deptId, boolean includeDeptId) {
        List<SysDept> sysDepts = baseMapper.listChildDept(deptId);
        if (!includeDeptId) {
            sysDepts.removeIf(it -> it.getId() == deptId);
        }
        return sysDepts;
    }

    /**
     * 获取生效中的父级部门的子部门
     */
    @Override
    public List<SysDept> listEnableChildDept(long deptId, boolean includeDeptId) {
        List<SysDept> sysDepts = baseMapper.listEnableChildDept(deptId);
        if (!includeDeptId) {
            sysDepts.removeIf(it -> it.getId() == deptId);
        }
        return sysDepts;
    }

    /**
     * 获取部门标签数据集合
     */
    @Override
    public List<LabelResp> listDeptLabel(List<Long> deptIds) {
        if (deptIds.isEmpty()) {
            return new ArrayList<>();
        }
        return lambdaQuery()
                .select(SysDept::getId, SysDept::getDeptName, SysDept::getDeptOrder)
                .in(SysDept::getId, deptIds)
                .list().stream()
                .sorted(Comparator.comparing(SysDept::getDeptOrder))
                .map(SysDept::toLabelResp)
                .collect(Collectors.toList());
    }

    /**
     * 校验父级部门id合法性(新增或是修改)
     */
    private void checkParentIdLegal(Long id, long parentId) {
        // 顶级部门直接放行
        if (CommonConstant.TOP_VIRTUAL_ID == parentId) {
            return;
        }
        // 父级部门不存在
        SysDept sysDept = getById(parentId);
        if (null == sysDept) {
            throw new SysException(SysStatus.DEPT_PARENT_NOT_EXITS);
        }
        // 父级部门被删除或是停用
        if (EnabledType.DISABLE.value.equals(sysDept.getEnabled()) || DataStatus.DISABLE.value.equals(sysDept.getDataStatus())) {
            throw new SysException(SysStatus.DEPT_NOT_ENABLE);
        }

        // 如果是新增则无需进行下面的判定
        if (null == id) {
            return;
        }
        // 父级部门不能自己或是原来的下级部门
        List<Long> childIds = getEnableChildDeptIds(id, true);
        if (childIds.contains(parentId)) {
            throw new SysException(SysStatus.DEPT_PARENT_IS_CHILD);
        }
    }

    /**
     * 校验部门编码合法性(新增或是修改)
     */
    private void checkDeptCodeLegal(Long id, String deptCode) {
        // 编码如果没有填写直接放行
        if (StrUtil.isEmpty(deptCode)) {
            return;
        }
        boolean exist = lambdaQuery()
                .eq(SysDept::getEnabled, EnabledType.ENABLE.value)
                .eq(SysDept::getDeptCode, deptCode)
                .ne(null != id, SysDept::getId, id)
                .exists();
        if (exist) {
            throw new SysException(SysStatus.DEPT_CODE_EXITS);
        }
    }

    /**
     * 获取部门的所有父级部门id集合
     */
    @Override
    public List<Long> getParentDeptIds(long deptId, boolean includeDeptId) {
        List<Long> ids = baseMapper.getParentDeptIds(deptId);
        if (!includeDeptId) {
            ids.remove(deptId);
        }
        return ids;
    }

}

