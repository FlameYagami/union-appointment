package com.gk.server.model.entity.system;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.DataStatus;
import com.gk.common.enums.SysRoleCode;
import com.gk.common.enums.YesOrNo;
import com.gk.common.model.base.BaseEntity;
import com.gk.framework.manager.RoleCacheManager;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.model.entity.system.SysUserDept;
import com.gk.framework.model.entity.system.SysUserRole;
import com.gk.server.enums.SysUserType;
import com.gk.server.model.dto.system.sysRegister.SysRegisterResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户注册表 数据库模型
 *
 * @author Flame
 * @since 2024-05-13 09:30:02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_register")
public class SysRegister extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 用户类型(从2开始自行修改)
     */
    private String userType;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String nickname;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 证件类型(01:身份证, 03:护照, 99:其他)
     */
    private String cardType;

    /**
     * 证件号
     */
    private String cardNumber;

    /**
     * 审核状态(2:待审核, 3:通过, 4:驳回)
     */
    private String reviewStatus;

    /**
     * 审核人
     */
    private Long reviewBy;

    /**
     * 审核时间
     */
    private Date reviewTime;

    /**
     * 备注
     */
    private String remark;


    public SysRegisterResp toResp(SysUser reviewSysUser) {
        SysRegisterResp resp = new SysRegisterResp()
                .setId(id)
                .setUserType(userType)
                .setUsername(username)
                .setNickname(nickname)
                .setTelephone(telephone)
                .setCardType(cardType)
                .setCardNumber(cardNumber)
                .setReviewStatus(reviewStatus)
                .setRemark(remark);
        if (null != reviewSysUser) {
            resp.setReviewUsername(reviewSysUser.getUsername())
                    .setReviewNickname(reviewSysUser.getNickname());
        }
        return resp;
    }

    /**
     * 转换成数据库操作类
     */
    public SysUser toSysUser() {
        return new SysUser()
                .setUsername(StrUtil.trim(username))
                .setPassword(password)
                .setPasswordChanged(YesOrNo.YES.value)
                .setNickname(nickname)
                .setDataStatus(DataStatus.NORMAL.value);
    }

    /**
     * 转换成数据库操作类
     */
    public SysUserInfo toSysUserInfo(long userId) {
        Date birthday = null;
        boolean isIdcard = IdcardUtil.isValidCard(cardNumber);
        if (isIdcard) {
            birthday = IdcardUtil.getBirthDate(cardNumber);
        }
        return new SysUserInfo()
                .setUserId(userId)
                .setUserType(userType)
                .setUsername(username)
                .setNickname(nickname)
                .setTelephone(telephone)
                .setCardType(cardType)
                .setCardNumber(cardNumber)
                .setBirthday(birthday);
    }

    /**
     * 转换成数据库操作类
     */
    public SysUserDept toSysUserDept(long userId) {
        long deptId = CommonConstant.TOP_ID;
        // todo 根据业务自行修改默认的机构
        if (SysUserType.STAFF.value.equals(userType)) {
            deptId = CommonConstant.TOP_ID;
        }
        return new SysUserDept()
                .setUserId(userId)
                .setDeptId(deptId);
    }

    /**
     * 转换成数据库操作类
     */
    public SysUserRole toSysUserRole(long userId) {
        long roleId = CommonConstant.TOP_ID;
        // todo 根据业务自行修改默认的角色
        if (SysUserType.STAFF.value.equals(userType)) {
            roleId = RoleCacheManager.getInstance().findByRoleCode(SysRoleCode.STAFF.value).getId();
        }
        return new SysUserRole()
                .setUserId(userId)
                .setRoleId(roleId);
    }

}
