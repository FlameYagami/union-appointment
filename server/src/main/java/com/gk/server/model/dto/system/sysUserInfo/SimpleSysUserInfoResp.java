package com.gk.server.model.dto.system.sysUserInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 简易系统用户信息请求类
 *
 * @author Flame
 * @since 2024-05-11 10:36
 **/
@ApiModel("简易系统用户信息请求")
@Data
@Accessors(chain = true)
public class SimpleSysUserInfoResp {

    @ApiModelProperty(value = "用户信息id", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "头像链接", example = "https://domain.com/files/avatar/20220101/5ea69f99-ae68-49ce-b144-8837d636b733.jpg")
    private String avatarUrl;

    @ApiModelProperty(value = "用户类型(1:管理员)", example = "1")
    private String userType;

    @ApiModelProperty(value = "账号", example = "admin")
    private String username;

    @ApiModelProperty(value = "姓名", example = "超管")
    private String nickname;

    @ApiModelProperty(value = "是否需要填写用户信息(true:是, false:否)", example = "false")
    private boolean needUserInfo;

}
