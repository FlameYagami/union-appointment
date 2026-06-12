package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.common.model.dto.LabelResp;
import com.gk.framework.model.bo.system.sysUser.SysUserExt;
import com.gk.framework.model.dto.system.sysUser.PasswordAlterReq;
import com.gk.framework.model.dto.system.sysUser.PasswordChangeReq;
import com.gk.framework.model.dto.system.sysUser.UserStatusChangeReq;
import com.gk.framework.model.entity.system.SysUser;

import java.util.List;

/**
 * 用户表 服务接口类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 单个查询
     *
     * @param userId 主键id
     * @return 数据库操作类
     */
    SysUser findByUserId(long userId);

    /**
     * 通过账号查询用户信息
     *
     * @param username 账号
     * @return 响应类
     */
    SysUser findByUsername(String username);

    void changeUserStatus(UserStatusChangeReq req);

    SysUserExt findSysUserExt(long userId);

    String resetPassword(long id);

    void changePassword(PasswordChangeReq req);

    void alterPassword(PasswordAlterReq req);

    List<LabelResp> listMgmtRoleLabel();

}
