package com.gk.security.model.dto.captcha;

import com.gk.security.model.dto.captcha.base.BaseCaptchaGetResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 点选文字验证码响应类
 *
 * @author GuoYu
 * @since 2023-08-22 16:18
 **/
@ApiModel("点选文字验证码响应")
@Data
@Accessors(chain = true)
public class ClickWordGetResp extends BaseCaptchaGetResp {

    @ApiModelProperty(value = "点选文字答案", example = "好,你,啊")
    private List<String> wordList;

}
