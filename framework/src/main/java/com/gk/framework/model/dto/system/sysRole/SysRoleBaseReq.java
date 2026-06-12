package com.gk.framework.model.dto.system.sysRole;

import com.gk.common.enums.DataScopeType;
import com.gk.common.enums.DataStatus;
import com.gk.common.enums.YesOrNo;
import com.gk.framework.model.entity.system.SysRole;
import com.gk.framework.model.entity.system.SysRoleMenu;
import com.gk.framework.validate.InEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色 基础请求类
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */
@Data
public class SysRoleBaseReq {

    @ApiModelProperty(value = "角色名称[32]", required = true, example = "角色A")
    @NotBlank(message = "角色名称不能为空")
    @Length(max = 32, message = "角色名称最多输入32个字符")
    private String roleName;

    @ApiModelProperty(value = "角色编码[64]", required = true, example = "admin")
    @NotBlank(message = "角色编码不能为空")
    @Length(max = 64, message = "角色编码最多输入64个字符")
    private String roleCode;

    @ApiModelProperty(value = "数据权限(1:全平台数据, 2:部门及以下数据, 3:本部门数据, 4:个人数据)", required = true, example = "数据权限")
    @NotBlank(message = "数据权限不能为空")
    @InEnum(DataScopeType.class)
    private String dataScope;

    @ApiModelProperty(value = "级联勾选(1:是, 0:否)", required = true, example = "1")
    @NotBlank(message = "级联勾选不能为空")
    @InEnum(YesOrNo.class)
    private String cascadeChecked;

    @ApiModelProperty(value = "角色级别", required = true, example = "2")
    @Min(value = 2, message = "角色级别不合法")
    private int roleLevel;

    @ApiModelProperty(value = "角色描述", example = "角色描述")
    @Length(max = 800, message = "角色描述最多输入800个字符")
    private String roleDesc;

    @ApiModelProperty(value = "角色状态(1:正常, 0:停用)", required = true, example = "1")
    @NotBlank(message = "角色状态不能为空")
    @InEnum(DataStatus.class)
    private String dataStatus;

    @ApiModelProperty(value = "角色排序", required = true, example = "1")
    @Min(value = 1, message = "角色排序不合法")
    private int roleOrder;

    @ApiModelProperty(value = "菜单id集合", required = true, example = "[1,2]")
    @NotNull(message = "菜单id集合缺失")
    @Size(min = 1, message = "菜单id集合缺失")
    private List<Long> menuIds;

    /**
     * 转换成数据库操作类
     */
    protected SysRole toEntity() {
        return new SysRole()
                .setRoleName(roleName)
                .setRoleCode(roleCode)
                .setDataScope(dataScope)
                .setCascadeChecked(cascadeChecked)
                .setRoleLevel(roleLevel)
                .setRoleDesc(roleDesc)
                .setDataStatus(dataStatus)
                .setRoleOrder(roleOrder);
    }

    /**
     * 转换成数据库操作类
     */
    protected List<SysRoleMenu> toSysRoleMenus(long roleId) {
        return menuIds.stream()
                .map(menuId -> new SysRoleMenu()
                        .setRoleId(roleId)
                        .setMenuId(menuId)
                ).collect(Collectors.toList());
    }

}
