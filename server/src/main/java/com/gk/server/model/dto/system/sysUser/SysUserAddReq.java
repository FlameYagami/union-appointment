package com.gk.server.model.dto.system.sysUser;

import cn.hutool.core.util.StrUtil;
import com.gk.common.constant.RegexConstant;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.model.entity.system.SysUserDept;
import com.gk.framework.model.entity.system.SysUserRole;
import com.gk.framework.validate.InEnum;
import com.gk.server.enums.SysUserType;
import com.gk.server.model.entity.system.SysUserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 用户 新增请求类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@ApiModel(value = "用户新增请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserAddReq extends SysUserBaseReq {

    @ApiModelProperty(value = "账号[32]", required = true, example = "账号")
    @NotBlank(message = "账号不能为空")
    @Length(max = 32, message = "账号最多输入32个字符")
    private String username;

    @ApiModelProperty(value = "密码", required = true, example = "密码")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = RegexConstant.PASSWORD, message = "密码必须是8-16位的大小写字母、数字、特殊字符组合")
    private String password;

    @ApiModelProperty(value = "用户类型(1:管理员)", required = true, example = "1")
    @NotBlank(message = "用户类型不能为空")
    @InEnum(SysUserType.class)
    private String userType;

    /**
     * 转换成数据库操作类
     */
    @Override
    public SysUser toEntity() {
        return super.toEntity()
                .setUsername(StrUtil.trim(username))
                .setPassword(SysUser.encryptPassword(StrUtil.trim(password)));
    }

    /**
     * 转换成数据库操作类
     */
    public SysUserInfo toSysUserInfo(long userId) {
        return super.toSysUserInfo()
                .setUserId(userId)
                .setUserType(userType)
                .setUsername(StrUtil.trim(username));
    }

    /**
     * 转换成数据库操作类
     */
    @Override
    public List<SysUserDept> toSysUserDepts(long userId) {
        return super.toSysUserDepts(userId);
    }

    /**
     * 转换成数据库操作类
     */
    @Override
    public List<SysUserRole> toSysUserRoles(long userId) {
        return super.toSysUserRoles(userId);
    }

}
