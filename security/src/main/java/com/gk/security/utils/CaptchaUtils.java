package com.gk.security.utils;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.security.constant.CaptchaConstant;
import com.gk.security.enums.CaptchaType;
import com.gk.security.model.dto.captcha.BlockPuzzleGetResp;
import com.gk.security.model.dto.captcha.ClickWordGetResp;
import com.gk.security.model.dto.captcha.base.BaseCaptchaGetResp;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.nio.charset.StandardCharsets;

/**
 * 行为验证码工具类
 *
 * @author GuoYu
 * @since 2023-08-14 10:43
 **/
@Slf4j
public class CaptchaUtils {

    private CaptchaUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static BaseCaptchaGetResp createGetReq(String captchaType) {
        if (CaptchaType.BLOCK_PUZZLE.value.equals(captchaType)) {
            return new BlockPuzzleGetResp();
        }
        if (CaptchaType.CLICK_WORD.value.equals(captchaType)) {
            return new ClickWordGetResp();
        }
        throw new SysException(SysStatus.CAPTCHA_TYPE_INVALID);
    }

    public static String getRemoteId(HttpServletRequest request) {
        String forward = request.getHeader("X-Forwarded-For");
        String ip = getRemoteIpFromForward(forward);
        String ua = request.getHeader("user-agent");
        if (StrUtil.isNotEmpty(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }

    public static String getRemoteIpFromForward(String forward) {
        if (StrUtil.isNotEmpty(forward)) {
            String[] ipList = forward.split(CommonConstant.COMMA);
            return StrUtil.trim(ipList[0]);
        }
        return null;
    }

    public static Font loadFont(String fontName, int fontStyle, int fontSize) {
        try {
            if (fontName.toLowerCase().endsWith(".ttf")
                    || fontName.toLowerCase().endsWith(".ttc")
                    || fontName.toLowerCase().endsWith(".otf")) {
                return Font.createFont(Font.TRUETYPE_FONT,
                        ResourceUtil.getStream("fonts/" + fontName)).deriveFont(fontStyle, (float)fontSize);
            } else {
                return new Font(fontName, fontStyle, fontSize);
            }
        } catch (Exception e) {
            log.error("Load font error", e);
            throw new SysException(SysStatus.FONT_LOAD_FAIL);
        }
    }

    public static int getEnOrChLength(String s) {
        int enCount = 0;
        int chCount = 0;
        for (int i = 0; i < s.length(); i++) {
            int length = String.valueOf(s.charAt(i)).getBytes(StandardCharsets.UTF_8).length;
            if (length > 1) {
                chCount++;
            } else {
                enCount++;
            }
        }
        int chOffset = (CaptchaConstant.HAN_ZI_HALF_SIZE) * chCount + 5;
        int enOffset = enCount * 8;
        return chOffset + enOffset;
    }
}