package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.framework.model.dto.system.sysUserRole.*;
import com.gk.framework.model.entity.system.SysUserRole;

import java.util.List;

/**
 * 用户角色关系表 服务接口类
 *
 * @author Flame
 * @since 2022-12-29 09:44:08
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 角色中用户分页查询
     *
     * @param req 查询条件
     * @return 分页列表
     */
    IPage<SysUserRolePageResp> pageUserList(SysUserRolePageReq req);

    /**
     * 角色中用户添加分页查询
     *
     * @param req 查询条件
     * @return 分页列表
     */
    IPage<SysUserRoleAddPageResp> pageUserAddList(SysUserRoleAddPageReq req);

    /**
     * 角色中添加用户
     *
     * @param req 请求类
     */
    void addUser(SysUserRoleAddReq req);

    /**
     * 角色中删除用户
     *
     * @param req 请求类
     */
    void deleteUser(SysUserRoleDeleteReq req);

    List<Long> getRoleIds(long userId);

    List<Long> getEnableRoleIds(long userId);

    boolean hasUseRole(long roleId);

    boolean modify(long userId, List<SysUserRole> sysUserRoles);

    void delete(long userId);

}
