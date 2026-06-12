package com.gk.framework.model.dto.system;

import com.gk.framework.model.entity.system.SysUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 混合SysUser/SysUserInfo的信息
 * 缓存在redis中方便读取用户的相关信息
 *
 * @author GuoYu
 * @since 2023-01-19 15:02
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class CachedUserData {

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public CachedUserData(SysUser sysUser) {
        this.userId = sysUser.getId();
        this.username = sysUser.getUsername();
        this.password = sysUser.getPassword();
    }
}
