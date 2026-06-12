package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户角色关系表 数据库模型
 *
 * @author Flame
 * @since 2022-12-29 09:44:08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysUserRole extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 用户表id
     */
    private Long userId;

    /**
     * 角色表id
     */
    private Long roleId;

}
