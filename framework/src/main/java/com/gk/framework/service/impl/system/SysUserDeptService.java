package com.gk.framework.service.impl.system;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.EnabledType;
import com.gk.common.utils.EntityUtils;
import com.gk.framework.mapper.system.SysUserDeptMapper;
import com.gk.framework.model.entity.system.SysUserDept;
import com.gk.framework.service.intf.system.ISysUserDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户部门关系表 服务实现类
 *
 * @author Flame
 * @since 2023-01-31 10:22:38
 */

@Service
@Slf4j
public class SysUserDeptService extends ServiceImpl<SysUserDeptMapper, SysUserDept> implements ISysUserDeptService {

    /**
     * 通过用户id查询拥有的所有部门id(包含停用)
     */
    @Override
    public List<Long> getDeptIds(long userId) {
        return lambdaQuery()
                .select(SysUserDept::getDeptId)
                .eq(SysUserDept::getEnabled, EnabledType.ENABLE.value)
                .eq(SysUserDept::getUserId, userId)
                .list().stream()
                .map(SysUserDept::getDeptId)
                .collect(Collectors.toList());
    }

    /**
     * 通过用户id查询拥有的启用的部门id集合
     */
    @Override
    public List<Long> getEnableDeptIds(long userId) {
        return baseMapper.getEnableDeptIdByUserId(userId);
    }

    /**
     * 修改userId关系的关系(新增、启用、删除)
     */
    @Override
    public boolean modify(long userId, List<SysUserDept> sysUserDepts) {
        // 查询主表id关联的所有数据
        List<SysUserDept> dbEntities = lambdaQuery()
                .eq(SysUserDept::getUserId, userId)
                .list();
        // 获取最终需要保存的数据
        List<SysUserDept> saveEntities = EntityUtils.generateRelEntity(sysUserDepts, dbEntities, SysUserDept::getDeptId);
        if (CollUtil.isEmpty(saveEntities)) {
            return true;
        }
        // 保存数据
        return saveOrUpdateBatch(saveEntities);
    }

    /**
     * 删除userId关联的数据
     */
    @Override
    public void delete(long userId) {
        boolean result = lambdaUpdate()
                .set(SysUserDept::getEnabled, EnabledType.DISABLE.value)
                .eq(SysUserDept::getUserId, userId)
                .update();
        if (!result) {
            log.warn("Delete SysUserDept Error: userId({})", userId);
        }
    }

    /**
     * 通过部门id查询所属这个部门的所有用户id集合
     */
    @Override
    public List<Long> getUserIdByDeptId(long deptId) {
        return lambdaQuery()
                .select(SysUserDept::getUserId)
                .eq(SysUserDept::getEnabled, EnabledType.ENABLE.value)
                .eq(SysUserDept::getDeptId, deptId)
                .list().stream()
                .map(SysUserDept::getUserId)
                .collect(Collectors.toList());
    }

    /**
     * 是否使用部门
     */
    @Override
    public boolean hasUseDept(long deptId) {
        return lambdaQuery()
                .eq(SysUserDept::getEnabled, EnabledType.ENABLE.value)
                .eq(SysUserDept::getDeptId, deptId)
                .exists();
    }


}

