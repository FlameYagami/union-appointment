package com.gk.server.model.dto.system.sysUser;

import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.model.entity.system.SysUserDept;
import com.gk.framework.model.entity.system.SysUserRole;
import com.gk.server.model.entity.system.SysUserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * 用户 修改请求类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@ApiModel(value = "用户修改请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserEditReq extends SysUserBaseReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000002L, message = "id不合法") // 超管角色校验
    private long id;

    /**
     * 转换成数据库操作类
     */
    @Override
    public SysUser toEntity() {
        return super.toEntity()
                .setId(id);
    }

    /**
     * 转换成数据库操作类
     */
    public SysUserInfo toSysUserInfo(long userInfoId) {
        return super.toSysUserInfo()
                .setId(userInfoId);
    }

    /**
     * 转换成数据库操作类
     */
    public List<SysUserDept> toSysUserDepts() {
        return super.toSysUserDepts(id);
    }

    /**
     * 转换成数据库操作类
     */
    public List<SysUserRole> toSysUserRoles() {
        return super.toSysUserRoles(id);
    }

}
