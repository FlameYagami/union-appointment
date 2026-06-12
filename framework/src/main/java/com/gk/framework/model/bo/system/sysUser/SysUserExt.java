package com.gk.framework.model.bo.system.sysUser;

import lombok.Data;

import java.util.Date;

/**
 * 用户扩展信息
 *
 * @author Flame
 * @date 2023-02-24 17:27
 **/
@Data
public class SysUserExt {

    /**
     * 主键
     */
    private Long id;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 初始密码是否变更过(账号创建以及密码重置都将此值置为0)
     */
    private String passwordChanged;

    /**
     * 姓名
     */
    private String nickname;

    /**
     * 账号状态
     */
    private String dataStatus;

    /**
     * 所属部门
     */
    private String deptNames;

    /**
     * 拥有角色
     */
    private String roleNames;

    /**
     * 创建时间
     */
    private Date createTime;


}
