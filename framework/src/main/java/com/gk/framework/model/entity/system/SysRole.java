package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.enums.DataStatus;
import com.gk.common.model.base.BaseEntity;
import com.gk.common.model.dto.LabelResp;
import com.gk.framework.model.dto.system.sysRole.SysRoleLabelResp;
import com.gk.framework.model.dto.system.sysRole.SysRoleResp;
import com.gk.framework.utils.DescUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色表 数据库模型
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 数据权限
     */
    private String dataScope;

    /**
     * 级联勾选
     */
    private String cascadeChecked;

    /**
     * 角色级别
     */
    private Integer roleLevel;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 是否为系统数据(1:是, 0:否, 默认:0)
     */
    private String systemData;

    /**
     * 角色状态
     */
    private String dataStatus;

    /**
     * 角色排序
     */
    private Integer roleOrder;


    public SysRoleResp toResp() {
        return new SysRoleResp()
                .setId(id)
                .setRoleName(roleName)
                .setRoleCode(roleCode)
                .setDataScope(dataScope)
                .setCascadeChecked(cascadeChecked)
                .setRoleLevel(roleLevel)
                .setRoleDesc(roleDesc)
                .setSystemData(systemData)
                .setDataStatus(dataStatus)
                .setRoleOrder(roleOrder);
    }

    public LabelResp toLabelResp() {
        return new LabelResp()
                .setId(id)
                .setName(roleName + DescUtils.getDataStatusDesc(dataStatus))
                .setDisabled(DataStatus.DISABLE.value.equals(dataStatus));
    }

    public SysRoleLabelResp toSysRoleLabelResp() {
        return new SysRoleLabelResp()
                .setId(id)
                .setName(roleName + DescUtils.getDataStatusDesc(dataStatus))
                .setRoleCode(roleCode)
                .setDisabled(DataStatus.DISABLE.value.equals(dataStatus));
    }

}
