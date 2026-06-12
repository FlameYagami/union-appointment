package com.gk.server.model.dto.system.sysUser;

import cn.hutool.core.util.StrUtil;
import com.gk.common.enums.DataStatus;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.model.entity.system.SysUserDept;
import com.gk.framework.model.entity.system.SysUserRole;
import com.gk.framework.validate.InEnum;
import com.gk.server.model.entity.system.SysUserInfo;
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
 * 用户 基础请求类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@Data
public class SysUserBaseReq {

    @ApiModelProperty(value = "姓名[32]", required = true, example = "姓名")
    @NotBlank(message = "姓名不能为空")
    @Length(max = 32, message = "姓名最多输入32个字符")
    private String nickname;

    @ApiModelProperty(value = "所属部门id集合", required = true, example = "[1000000000000000001, 1000000000000000002]")
    @NotNull(message = "请选择1~5个部门")
    @Size(min = 1, max = 5, message = "请选择1~5个部门")
    private List<@Min(value = 1000000000000000001L, message = "id不合法") Long> deptIds;

    @ApiModelProperty(value = "角色id集合", required = true, example = "[1000000000000000001, 1000000000000000002]")
    @NotNull(message = "请选择1~5个角色")
    @Size(min = 1, max = 5, message = "请选择1~5个角色")
    private List<@Min(value = 1000000000000000002L, message = "id不合法") Long> roleIds;

    @ApiModelProperty(value = "账号状态(1:正常, 0:停用)", required = true, example = "1")
    @NotBlank(message = "账号状态不能为空")
    @InEnum(DataStatus.class)
    private String dataStatus;

    /**
     * 转换成数据库操作类
     */
    protected SysUser toEntity() {
        return new SysUser()
                .setNickname(StrUtil.trim(nickname))
                .setDataStatus(dataStatus);
    }

    /**
     * 转换成数据库操作类
     */
    protected SysUserInfo toSysUserInfo() {
        return new SysUserInfo()
                .setNickname(StrUtil.trim(nickname));
    }

    /**
     * 转换成数据库操作类
     */
    protected List<SysUserDept> toSysUserDepts(long userId) {
        return deptIds.stream()
                .map(deptId -> new SysUserDept()
                        .setUserId(userId)
                        .setDeptId(deptId)
                ).collect(Collectors.toList());
    }

    /**
     * 转换成数据库操作类
     */
    protected List<SysUserRole> toSysUserRoles(long userId) {
        return roleIds.stream()
                .map(deptId -> new SysUserRole()
                        .setUserId(userId)
                        .setRoleId(deptId)
                ).collect(Collectors.toList());
    }

}
