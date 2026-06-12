package com.gk.framework.service.impl.system;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.EntityUtils;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.annotation.DataScope;
import com.gk.framework.helper.SysUserHelper;
import com.gk.framework.mapper.system.SysUserRoleMapper;
import com.gk.framework.model.dto.system.sysUserRole.*;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.model.entity.system.SysUserRole;
import com.gk.framework.service.intf.system.ISysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户角色关系表 服务实现类
 *
 * @author Flame
 * @since 2022-12-29 09:44:08
 */

@Service
@Slf4j
public class SysUserRoleService extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    /**
     * 角色中用户分页查询
     */
    @Override
    public IPage<SysUserRolePageResp> pageUserList(SysUserRolePageReq req) {
        IPage<SysUserRolePageResp> resp = baseMapper.pageSysRoleUser(req.createPage(), req);
        resp.getRecords().forEach(SysUserRolePageResp::lateInit);
        return resp;
    }


    /**
     * 角色中用户添加分页查询
     */
    @DataScope(bizTableAlias = "sud", userIdAlias = "id")
    @Override
    public IPage<SysUserRoleAddPageResp> pageUserAddList(SysUserRoleAddPageReq req) {
        IPage<SysUserRoleAddPageResp> resp = baseMapper.pageSysRoleUserAdd(req.createPage(), req);
        resp.getRecords().forEach(SysUserRoleAddPageResp::lateInit);
        return resp;
    }

    /**
     * 角色中用户新增
     */
    @Override
    public void addUser(SysUserRoleAddReq req) {
        // 查询需要在当前角色下添加所有用户的拥有的角色数据(包括废弃的关系)
        List<SysUserRole> dbEntities = lambdaQuery()
                .in(SysUserRole::getUserId, req.getUserIds())
                .list();
        // 以 userId 为 key, 持有的 roleId 的数量为 value 转换为map
        Map<Long, Long> userRoleCountMap = dbEntities.stream()
                .filter(it -> EnabledType.ENABLE.value.equals(it.getEnabled()))
                .filter(it -> it.getRoleId() != req.getRoleId()) // 排除本身要添加的角色数据才能判定添加的角色有没有超过上限
                .collect(Collectors.groupingBy(SysUserRole::getUserId, Collectors.counting()));
        // 判定用户持有的角色是否超过上限
        userRoleCountMap.forEach((key, value) -> {
            if (value >= 5) {
                SysUser sysUser = SysUserHelper.getInstance().findByUserId(key);
                throw new SysException(sysUser.getUsername() + SysStatus.ROLE_EXCEEDS_UPPER_LIMIT.getMessage());
            }
        });
        // 保存关联关系
        List<SysUserRole> sysUserRoles = req.toSysUserRoles();
        // 获取最终需要保存的数据
        List<SysUserRole> linkEntities = dbEntities.stream()
                .filter(it -> it.getRoleId() == req.getRoleId())
                .collect(Collectors.toList());
        List<SysUserRole> saveEntities = EntityUtils.generateRelEntity(sysUserRoles, linkEntities, SysUserRole::getUserId);
        // 保存数据
        if (!saveOrUpdateBatch(saveEntities)) {
            log.error("Add SysUserRole Error in add SysUserRole: {}", JsonUtils.toJson(sysUserRoles));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 角色中用户删除
     */
    @Override
    public void deleteUser(SysUserRoleDeleteReq req) {
        // 校验是否存在多个角色, 如果只有一个, 则不允许删除
        boolean onlyOneRole = 1 >= lambdaQuery()
                .eq(SysUserRole::getEnabled, EnabledType.ENABLE.value)
                .eq(SysUserRole::getUserId, req.getUserId())
                .count();
        if (onlyOneRole) {
            throw new SysException(SysStatus.ROLE_HAS_ONLY_ONE);
        }
        // 删除关联关系
        boolean result = lambdaUpdate()
                .set(SysUserRole::getEnabled, EnabledType.DISABLE.value)
                .eq(SysUserRole::getUserId, req.getUserId())
                .eq(SysUserRole::getRoleId, req.getRoleId())
                .update();
        if (!result) {
            log.error("Delete SysUserRole Error: {}", JsonUtils.toJson(req));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 通过用户id查询拥有的所有角色id(包含停用)
     */
    @Override
    public List<Long> getRoleIds(long userId) {
        return lambdaQuery()
                .select(SysUserRole::getRoleId)
                .eq(SysUserRole::getEnabled, EnabledType.ENABLE.value)
                .eq(SysUserRole::getUserId, userId)
                .list().stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
    }

    /**
     * 通过用户id查询拥有的启用的角色id集合
     */
    @Override
    public List<Long> getEnableRoleIds(long userId) {
        return baseMapper.getEnableRoleIdByUserId(userId);
    }

    /**
     * 判断角色是否被用户使用
     */
    @Override
    public boolean hasUseRole(long roleId) {
        return lambdaQuery()
                .eq(SysUserRole::getEnabled, EnabledType.ENABLE.value)
                .eq(SysUserRole::getRoleId, roleId)
                .exists();
    }

    /**
     * 修改userId关系的关系(新增、启用、删除)
     */
    @Override
    public boolean modify(long userId, List<SysUserRole> sysUserRoles){
        // 查询主表id关联的所有数据
        List<SysUserRole> dbEntities = lambdaQuery()
                .eq(SysUserRole::getUserId, userId)
                .list();
        // 获取最终需要保存的数据
        List<SysUserRole> saveEntities = EntityUtils.generateRelEntity(sysUserRoles, dbEntities, SysUserRole::getRoleId);
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
                .set(SysUserRole::getEnabled, EnabledType.DISABLE.value)
                .eq(SysUserRole::getUserId, userId)
                .update();
        if (!result) {
            log.warn("Delete SysUserRole Error: userId({})", userId);
        }
    }


}

