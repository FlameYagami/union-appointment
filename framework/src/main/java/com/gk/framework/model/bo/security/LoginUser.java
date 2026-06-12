package com.gk.framework.model.bo.security;

import com.gk.framework.model.dto.system.CachedUserData;
import com.gk.framework.model.entity.system.SysUser;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 认证业务模型类
 *
 * @author Flame
 * @since 2022-11-20 10:16
 */

@Data
@Accessors(chain = true)
public class LoginUser implements UserDetails {

    /**
     * 用户Id
     */
    private long userId;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public LoginUser() {}

    public LoginUser(CachedUserData cachedUserData) {
        this.userId = cachedUserData.getUserId();
        this.username = cachedUserData.getUsername();
        this.password = cachedUserData.getPassword();
    }

    public LoginUser setSysUser(SysUser sysUser) {
        this.userId = sysUser.getId();
        this.username = sysUser.getUsername();
        this.password = sysUser.getPassword();
        return this;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
