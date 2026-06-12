package com.gk.security.model.dto.captcha;

import com.gk.security.model.dto.captcha.base.BaseCaptchaGetResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 滑动验证码响应类
 *
 * @author GuoYu
 * @since 2023-08-22 16:11
 **/
@ApiModel("滑动验证码响应")
@Data
@Accessors(chain = true)
public class BlockPuzzleGetResp extends BaseCaptchaGetResp {

    @ApiModelProperty(value = "滑块图片", example = "base64图片字符串")
    private String slideBlockImage;
}
