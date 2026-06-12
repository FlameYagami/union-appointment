package com.gk.security.utils;

import com.gk.common.utils.StringExtUtils;
import com.gk.security.enums.CaptchaImage;
import com.gk.security.constant.CaptchaConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 行为验证码工具类
 *
 * @author GuoYu
 * @since 2023-08-14 10:43
 **/
@Slf4j
public class CaptchaImageUtils {
    // 滑块底图
    private static final Map<String, String> slideBgMap = new ConcurrentHashMap<>();
    // 滑块图片
    private static final Map<String, String> slideBlockMap = new ConcurrentHashMap<>();
    // 点选文字底图
    private static final Map<String, String> clickWordBgMap = new ConcurrentHashMap<>();

    private static final Map<String, String[]> filenameMap = new ConcurrentHashMap<>();

    public static void cacheCaptchaImage(Map<String, String> slideBgImageMap, Map<String, String> slideBlockImageMap, Map<String, String> clickWordBgImageMap) {
        slideBgMap.putAll(slideBgImageMap);
        slideBlockMap.putAll(slideBlockImageMap);
        clickWordBgMap.putAll(clickWordBgImageMap);
        filenameMap.put(CaptchaImage.SLIDE_BACKGROUND.value, slideBgMap.keySet().toArray(new String[0]));
        filenameMap.put(CaptchaImage.SLIDE_BLOCK.value, slideBlockMap.keySet().toArray(new String[0]));
        filenameMap.put(CaptchaImage.CLICK_WORD_BACKGROUND.value, clickWordBgMap.keySet().toArray(new String[0]));
    }

    /**
     * 获取滑动验证码背景图
     */
    public static BufferedImage getSlideBgImage() {
        String[] imageArr = filenameMap.get(CaptchaImage.SLIDE_BACKGROUND.value);
        if (null == imageArr || imageArr.length == 0) {
            return null;
        }
        int randomInt = StringExtUtils.randomNumber(imageArr.length);
        String imageStr = slideBgMap.get(imageArr[randomInt]);
        return getBase64StrImage(imageStr);
    }

    /**
     * 获取滑动验证码滑块图片
     */
    public static String getSlideBlockImage() {
        String[] imageArr = filenameMap.get(CaptchaImage.SLIDE_BLOCK.value);
        if (null == imageArr || imageArr.length == 0) {
            return null;
        }
        int randomInt = StringExtUtils.randomNumber(imageArr.length);
        return slideBlockMap.get(imageArr[randomInt]);
    }

    /**
     * 获取点选文字背景图
     */
    public static BufferedImage getClickWordImage() {
        String[] imageArr = filenameMap.get(CaptchaImage.CLICK_WORD_BACKGROUND.value);
        if (null == imageArr || imageArr.length == 0) {
            return null;
        }
        int randomInt = StringExtUtils.randomNumber(imageArr.length);
        String imageStr = clickWordBgMap.get(imageArr[randomInt]);
        return getBase64StrImage(imageStr);
    }

    /**
     * 图片转base64 字符串
     */
    public static String getImageBase64Str(BufferedImage image) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, CaptchaConstant.IMAGE_TYPE_PNG, outputStream);
            byte[] bytes = outputStream.toByteArray();
            return Base64Utils.encodeToString(bytes).trim();
        } catch (IOException e) {
            log.error("Convert image to base64 string error: ", e);
        }
        return "";
    }

    /**
     * base64 字符串转图片
     */
    public static BufferedImage getBase64StrImage(String base64String) {
        try {
            byte[] bytes = Base64Utils.decodeFromString(base64String);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            log.error("Convert base64 string to image error: ", e);
        }
        return null;
    }

    /**
     * 设置图片水印
     */
    public static void setupWatermark(BufferedImage bufferedImage, String watermark, int watermarkFontStyle, String watermarkFontName) {
        Graphics backgroundGraphics = bufferedImage.getGraphics();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        Font watermarkFont = CaptchaUtils.loadFont(watermarkFontName, watermarkFontStyle, CaptchaConstant.HAN_ZI_HALF_SIZE);
        backgroundGraphics.setFont(watermarkFont);
        backgroundGraphics.setColor(Color.white);
        backgroundGraphics.drawString(watermark, width - CaptchaUtils.getEnOrChLength(watermark), height - CaptchaConstant.HAN_ZI_HALF_SIZE + 7);
    }

}
