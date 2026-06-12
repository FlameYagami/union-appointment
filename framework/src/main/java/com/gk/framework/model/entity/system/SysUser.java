package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 用户表 数据库模型
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 账号
     */
    private String username;

    /**
     * 姓名
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 初始密码是否变更过(账号创建以及密码重置都将此值置为0)
     */
    private String passwordChanged;

    /**
     * 黑名单状态
     */
    private String blacklistStatus;

    /**
     * 是否为系统数据(1:是, 0:否, 默认:0)
     */
    private String systemData;

    /**
     * 账号状态
     */
    private String dataStatus;

    /**
     * 生成BCryptPasswordEncoder密码
     */
    public static String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

}
