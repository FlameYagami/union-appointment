package com.gk.security.model.bo;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.gk.security.utils.CaptchaUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;

/**
 * @author GuoYu
 * @since 2023-08-22 17:40
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaInfo {
    /**
     * 客户端Uid
     */
    @ApiModelProperty(hidden = true)
    private String clientUid;

    /***
     * 浏览器请求信息 ip+userAgent
     */
    @ApiModelProperty(hidden = true)
    private String browserInfo;

    public void handleData(HttpServletRequest request) {
        this.browserInfo = CaptchaUtils.getRemoteId(request);
        if (StrUtil.isEmpty(this.clientUid) && StrUtil.isNotEmpty(this.browserInfo)) {
            this.clientUid = SecureUtil.md5(this.browserInfo);
        }
    }
}
