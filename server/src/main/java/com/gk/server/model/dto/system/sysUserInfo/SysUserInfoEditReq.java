package com.gk.server.model.dto.system.sysUserInfo;

import cn.hutool.core.util.IdcardUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import com.gk.common.constant.RegexConstant;
import com.gk.common.enums.CardType;
import com.gk.framework.constant.DictConstant;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.validate.InDict;
import com.gk.framework.validate.InEnum;
import com.gk.server.model.entity.system.SysUserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 用户信息 修改请求类
 *
 * @author Flame
 * @since 2022-12-16 17:46:27
 */
@ApiModel(value = "用户信息修改请求")
@Data
public class SysUserInfoEditReq {

    @ApiModelProperty(value = "主键id(userInfoId)", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long id;

    @ApiModelProperty(value = "姓名[32]", example = "管理员")
    @NotBlank(message = "姓名不能为空")
    @Length(max = 32, message = "姓名最多输入32个字符")
    private String nickname;

    @ApiModelProperty(value = "性别(1:男, 2:女, 9:未知)", example = "1")
    @NotBlank(message = "性别不能为空")
    @InDict(DictConstant.CODE_GENDER)
    private String gender;

    @ApiModelProperty(value = "手机号", example = "15800000000")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = RegexConstant.TELEPHONE, message = "请输入正确的手机号")
    private String telephone;

    @ApiModelProperty(value = "证件类型(1:身份证, 2:护照, 9:其他)", required = true, example = "1")
    @NotBlank(message = "证件类型不能为空")
    @InEnum(CardType.class)
    private String cardType;

    @ApiModelProperty(value = "证件号[32]", required = true, example = "360000000000000000")
    @NotBlank(message = "证件号")
    @Length(max = 32, message = "证件号最多输入32个字符")
    private String cardNumber;

    @ApiModelProperty(value = "生日", example = "2000-01-31")
    @DateTimeFormat(pattern = DateConstant.DATE_PATTERN)
    @JsonFormat(pattern = DateConstant.DATE_PATTERN)
    private Date birthday;

    /**
     * 转换成数据库操作类
     */
    public SysUserInfo toEntity() {
        boolean isIdcard = IdcardUtil.isValidCard(cardNumber);
        if (isIdcard) {
            birthday = IdcardUtil.getBirthDate(cardNumber);
        }
        return new SysUserInfo()
                .setId(id)
                .setNickname(nickname)
                .setGender(gender)
                .setTelephone(telephone)
                .setCardType(cardType)
                .setCardNumber(cardNumber)
                .setBirthday(birthday);
    }

    /**
     * 转换成数据库操作类
     */
    public SysUser toSysUser(long userId) {
        return new SysUser()
                .setId(userId)
                .setNickname(nickname);
    }

}
