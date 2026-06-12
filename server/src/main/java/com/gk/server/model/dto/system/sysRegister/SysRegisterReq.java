package com.gk.server.model.dto.system.sysRegister;

import com.gk.common.constant.CommonConstant;
import com.gk.common.constant.RegexConstant;
import com.gk.common.enums.CardType;
import com.gk.framework.validate.InEnum;
import com.gk.server.enums.RegisterReviewStatus;
import com.gk.server.enums.RegisterUserType;
import com.gk.server.enums.SysUserType;
import com.gk.server.model.entity.system.SysRegister;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 注册 请求类
 *
 * @author Flame
 * @since 2024-05-13 09:30:02
 */
@ApiModel(value = "注册请求")
@Data
public class SysRegisterReq {

    @ApiModelProperty(value = "用户类型(2:示例角色)", required = true, example = "2")
    @NotBlank(message = "用户类型不能为空")
    @InEnum(RegisterUserType.class)
    private String userType;

    @ApiModelProperty(value = "账号[32]", required = true, example = "账号")
    @NotBlank(message = "账号不能为空")
    @Length(max = 32, message = "账号最多输入32个字符")
    private String username;

    @ApiModelProperty(value = "姓名[32]", required = true, example = "姓名")
    @NotBlank(message = "姓名不能为空")
    @Length(max = 32, message = "姓名最多输入32个字符")
    private String nickname;

    @ApiModelProperty(value = "密码", required = true, example = "密码")
    @NotBlank(message = "密码不能为空")
    private String password; // 这里的密码由外部代码判断

    @ApiModelProperty(value = "手机号", required = true, example = "15800000000")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = RegexConstant.TELEPHONE, message = "请输入正确的手机号")
    private String telephone;

    @ApiModelProperty(value = "证件类型(01:身份证, 03:护照, 99:其他)", required = true, example = "01")
    @NotBlank(message = "证件类型不能为空")
    @InEnum(CardType.class)
    private String cardType;

    @ApiModelProperty(value = "证件号[32]", required = true, example = "360000000000000000")
    @NotBlank(message = "证件号不能为空")
    @Length(max = 32, message = "证件号最多输入32个字符")
    private String cardNumber;

    /**
     * 前端生成的AES Key拼接上AES IV, 再由RSA的公钥加密后, 生成通关卡
     */
    @ApiModelProperty(value = "加密通关卡", required = true, example = "carnet")
    @NotBlank(message = "密码不能为空")
    private String carnet;

    public SysRegister toEntity(String password) {
        SysRegister sysRegister = new SysRegister()
                .setUserType(userType)
                .setUsername(username)
                .setPassword(encryptPassword(password))
                .setNickname(nickname)
                .setTelephone(telephone)
                .setCardType(cardType)
                .setCardNumber(cardNumber)
                .setReviewStatus(RegisterReviewStatus.PENDING.value);
        sysRegister.setCreateBy(CommonConstant.TOP_ID);
        sysRegister.setUpdateBy(CommonConstant.TOP_ID);
        // todo 根据用户类型判定是否需要填充其他信息
        if (SysUserType.isStaff(userType)) {

        }
        return sysRegister;
    }

    /**
     * 生成BCryptPasswordEncoder密码
     */
    public static String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

}
