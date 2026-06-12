package com.gk.server.model.entity.system;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.constant.CommonConstant;
import com.gk.common.model.base.BaseEntity;
import com.gk.framework.helper.SysFileHelper;
import com.gk.framework.model.bo.system.sysUser.SysUserExt;
import com.gk.framework.utils.LoginUserUtils;
import com.gk.server.model.dto.system.sysUserInfo.SimpleSysUserInfoResp;
import com.gk.server.model.dto.system.sysUserInfo.SysUserInfoResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户信息表 数据库模型
 *
 * @author Flame
 * @since 2022-12-16 17:46:27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysUserInfo extends BaseEntity {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 账号(从sys_user表拷贝)
     */
    private String username;

    /**
     * 姓名(从sys_user表拷贝)
     */
    private String nickname;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 性别
     */
    private String gender;

    /**
     * 手机号(敏感信息)
     */
    private String telephone;

    /**
     * 头像id
     */
    private Long avatarFid;

    /**
     * 证件类型
     */
    private String cardType;

    /**
     * 证件号(敏感信息)
     */
    private String cardNumber;

    /**
     * 生日
     */
    private Date birthday;

    public SimpleSysUserInfoResp toSysUserInfoSimpleResp() {
        String avatarUrl = null == avatarFid ? CommonConstant.EMPTY : SysFileHelper.getInstance().getFileUrl(avatarFid);
        // 手机号与证件号在系统内部创建时不需要填写, 因此作为是否需要首次登录补充信息的判断项
        boolean needUserInfo = StrUtil.isEmpty(telephone) || StrUtil.isEmpty(cardNumber);
        // 判定是否为顶级管理员
        boolean isTopAdmin = LoginUserUtils.isTopAdmin();
        return new SimpleSysUserInfoResp()
                .setId(id)
                .setAvatarUrl(avatarUrl)
                .setUserType(userType)
                .setUsername(username)
                .setNickname(nickname)
                .setNeedUserInfo(!isTopAdmin && needUserInfo);
    }

    public SysUserInfoResp toResp(SysUserExt sysUserExt) {
        String avatarUrl = null == avatarFid ? CommonConstant.EMPTY : SysFileHelper.getInstance().getFileUrl(avatarFid);
        return new SysUserInfoResp(sysUserExt)
                .setId(id)
                .setUserId(userId)
                .setUserType(userType)
                .setUsername(username)
                .setNickname(nickname)
                .setGender(gender)
                .setTelephone(telephone)
                .setAvatarUrl(avatarUrl)
                .setCardType(cardType)
                .setCardNumber(cardNumber)
                .setBirthday(birthday);
    }

}
