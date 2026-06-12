package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户部门关系表 数据库模型
 *
 * @author Flame
 * @since 2023-01-31 10:22:38
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysUserDept extends BaseEntity {

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
     * 部门表id
     */
    private Long deptId;


}
