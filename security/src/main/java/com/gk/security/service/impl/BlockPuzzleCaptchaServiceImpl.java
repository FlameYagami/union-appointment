package com.gk.security.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.AesUtils;
import com.gk.common.utils.JsonUtils;
import com.gk.common.utils.StringExtUtils;
import com.gk.security.constant.CaptchaConstant;
import com.gk.security.handler.CaptchaLimitHandler;
import com.gk.security.model.bo.CaptchaInfo;
import com.gk.security.model.bo.CaptchaParam;
import com.gk.security.model.bo.CaptchaPoint;
import com.gk.security.model.dto.captcha.BlockPuzzleGetResp;
import com.gk.security.model.dto.captcha.CaptchaCheckReq;
import com.gk.security.model.dto.captcha.base.BaseCaptchaGetResp;
import com.gk.security.service.intf.CaptchaCacheService;
import com.gk.security.service.intf.CaptchaService;
import com.gk.security.utils.CaptchaImageUtils;
import com.gk.security.utils.CaptchaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 滑动验证码 业务实现类
 * <p>
 * @author Kevin
 * @date 2023-08-11 15:06
 */
@Service
@Slf4j
public class BlockPuzzleCaptchaServiceImpl implements CaptchaService {

    @Resource
    private CaptchaParam captchaParam;

    @Resource
    private CaptchaLimitHandler captchaLimitHandler;

    @Resource
    private CaptchaCacheService captchaRedisService;



    @Override
    public BaseCaptchaGetResp get(CaptchaInfo captchaInfo) {
        BlockPuzzleGetResp getResp = (BlockPuzzleGetResp)CaptchaUtils.createGetReq(captchaParam.getCaptchaType());
        if (captchaParam.isReqLimit()) {
            captchaLimitHandler.validateGet(captchaInfo.getClientUid());
        }

        // 背景图片
        BufferedImage slideBgImage = CaptchaImageUtils.getSlideBgImage();
        if (null == slideBgImage) {
            log.error("滑动背景图初始化失败");
            throw new SysException(SysStatus.CAPTCHA_IMAGE_ERROR);
        }

        // 设置水印
        CaptchaImageUtils.setupWatermark(slideBgImage, captchaParam.getWatermark(), captchaParam.getFontStyle(), captchaParam.getWatermarkFont());

        // 拼图图片
        String slideBlockBase64 = CaptchaImageUtils.getSlideBlockImage();
        BufferedImage slideBlockImage = CaptchaImageUtils.getBase64StrImage(slideBlockBase64);
        if (null == slideBlockImage) {
            log.error("滑动拼图初始化失败");
            throw new SysException(SysStatus.CAPTCHA_IMAGE_ERROR);
        }
        pictureTemplatesCut(getResp, slideBgImage, slideBlockImage, slideBlockBase64);
        if (StrUtil.isEmpty(getResp.getBackgroundImage())
                || StrUtil.isEmpty(getResp.getSlideBlockImage())) {
            log.error("滑动验证码图片处理失败");
            throw new SysException(SysStatus.CAPTCHA_IMAGE_ERROR);
        }
        return getResp;
    }

    @Override
    public void check(CaptchaCheckReq checkReq) {
        if (captchaParam.isReqLimit()) {
            captchaLimitHandler.validateCheck(checkReq.getClientUid());
        }

        // 取坐标信息
        String checkKey = String.format(CaptchaConstant.CAPTCHA_CHECK_KEY, checkReq.getCheckId());
        String checkPoint = captchaRedisService.get(checkKey);
        if (!captchaRedisService.exists(checkKey) || StrUtil.isEmpty(checkPoint)) {
            throw new SysException(SysStatus.CAPTCHA_EXPIRE_ERROR);
        }
        // 验证码只用一次，即刻失效
        captchaRedisService.delete(checkKey);

        CaptchaPoint point = JsonUtils.toObject(checkPoint, CaptchaPoint.class);
        if (point == null || StrUtil.isEmpty(point.getSecretKey())) {
            captchaLimitHandler.afterValidateFail(checkReq.getClientUid());
            throw new SysException(SysStatus.CAPTCHA_EXPIRE_ERROR);
        }
        List<String> secretKeyList = StrUtil.split(point.getSecretKey(), CommonConstant.PIPE);
        if (secretKeyList.size() != CaptchaConstant.SECRET_KEY_SIZE) {
            captchaLimitHandler.afterValidateFail(checkReq.getClientUid());
            throw new SysException(SysStatus.CAPTCHA_CHECK_ERROR);
        }
        String aesKey = secretKeyList.get(0);
        String aesIv = secretKeyList.get(1);
        String reqPointJson = AesUtils.decrypt(checkReq.getPointJson(), aesKey, aesIv);
        CaptchaPoint reqPoint =  JsonUtils.toObject(reqPointJson, CaptchaPoint.class);
        if (reqPoint == null) {
            captchaLimitHandler.afterValidateFail(checkReq.getClientUid());
            throw new SysException(SysStatus.CAPTCHA_CHECK_ERROR);
        }
        int slipOffset = captchaParam.getSlipOffset();
        if (point.getX() - slipOffset > reqPoint.getX()
                || reqPoint.getX() > point.getX() + slipOffset
                || point.getY() != reqPoint.getY()) {
            captchaLimitHandler.afterValidateFail(checkReq.getClientUid());
            throw new SysException(SysStatus.CAPTCHA_CHECK_ERROR);
        }
        // 校验成功，将信息存入缓存
        String verifyEncrypt = AesUtils.encrypt(checkReq.getCheckId() + CaptchaConstant.VERIFY_SEPARATOR + reqPointJson, aesKey, aesIv);
        String verifyKeyContent = Base64Utils.encodeToString(verifyEncrypt.getBytes(StandardCharsets.UTF_8));
        String verifyKey = String.format(CaptchaConstant.CAPTCHA_VERIFY_KEY, verifyKeyContent);
        captchaRedisService.set(verifyKey, checkReq.getCheckId(), CaptchaConstant.EXPIRE_VERIFY_DURATION);
    }

    @Override
    public void verification(String captchaVerify) {
        if (StrUtil.isEmpty(captchaVerify)) {
            throw new SysException(SysStatus.CAPTCHA_CHECK_ERROR);
        }
        String verifyKey = String.format(CaptchaConstant.CAPTCHA_VERIFY_KEY, captchaVerify);
        if (!captchaRedisService.exists(verifyKey)) {
            throw new SysException(SysStatus.CAPTCHA_EXPIRE_ERROR);
        }
        //二次校验取值后，即刻失效
        captchaRedisService.delete(verifyKey);
    }

    /**
     * 根据模板切图
     */
    public void pictureTemplatesCut(BlockPuzzleGetResp getResp, BufferedImage backgroundImage, BufferedImage slideBlockImage, String slideBlockBase64) {
        try {
            int backgroundWidth = backgroundImage.getWidth();
            int backgroundHeight = backgroundImage.getHeight();
            int slideBlockWidth = slideBlockImage.getWidth();
            int slideBlockHeight = slideBlockImage.getHeight();

            //随机生成拼图坐标
            CaptchaPoint point = generateCaptchaPoint(backgroundWidth, backgroundHeight, slideBlockWidth, slideBlockHeight);
            int x = point.getX();
            int y = point.getY();

            // 生成新的拼图图像
            BufferedImage newSlideBlockImage = new BufferedImage(slideBlockWidth, slideBlockHeight, slideBlockImage.getType());
            Graphics2D graphics = newSlideBlockImage.createGraphics();

            int bold = 5;
            // 如果需要生成RGB格式，需要做如下配置,Transparency 设置透明
            newSlideBlockImage = graphics.getDeviceConfiguration().createCompatibleImage(slideBlockWidth, slideBlockHeight, Transparency.TRANSLUCENT);
            // 新建的图像根据模板颜色赋值,源图生成遮罩
            cutByTemplate(backgroundImage, slideBlockImage, newSlideBlockImage, x, 0);
            if (captchaParam.getInterferenceOptions() > 0) {
                int position;
                if (backgroundWidth - x - 5 > slideBlockWidth * 2) {
                    //在原扣图右边插入干扰图
                    position = StringExtUtils.randomNumber(x + slideBlockWidth + 5, backgroundWidth - slideBlockWidth);
                } else {
                    //在原扣图左边插入干扰图
                    position = StringExtUtils.randomNumber(100, x - slideBlockWidth - 5);
                }
                while (true) {
                    String slidingBlock = CaptchaImageUtils.getSlideBlockImage();
                    if (!slideBlockBase64.equals(slidingBlock)) {
                        interferenceByTemplate(backgroundImage, Objects.requireNonNull(CaptchaImageUtils.getBase64StrImage(slidingBlock)), position, 0);
                        break;
                    }
                }
            }
            if (captchaParam.getInterferenceOptions() > 1) {
                while (true) {
                    String sbImage = CaptchaImageUtils.getSlideBlockImage();
                    if (!slideBlockBase64.equals(sbImage)) {
                        int randomInt = StringExtUtils.randomNumber(slideBlockWidth, 100 - slideBlockWidth);
                        interferenceByTemplate(backgroundImage, Objects.requireNonNull(CaptchaImageUtils.getBase64StrImage(sbImage)), randomInt, 0);
                        break;
                    }
                }
            }

            // 设置“抗锯齿”的属性
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setStroke(new BasicStroke(bold, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            graphics.drawImage(newSlideBlockImage, 0, 0, null);
            graphics.dispose();

            ByteArrayOutputStream slideBlockOutputStream = new ByteArrayOutputStream();
            ImageIO.write(newSlideBlockImage, CaptchaConstant.IMAGE_TYPE_PNG, slideBlockOutputStream);
            ByteArrayOutputStream slideBgOutputStream = new ByteArrayOutputStream();
            ImageIO.write(backgroundImage, CaptchaConstant.IMAGE_TYPE_PNG, slideBgOutputStream);
            getResp.setBackgroundImage(Base64Utils.encodeToString(slideBgOutputStream.toByteArray()).replaceAll("[\r\n]", ""));
            getResp.setSlideBlockImage(Base64Utils.encodeToString(slideBlockOutputStream.toByteArray()).replaceAll("[\r\n]", ""));
            getResp.setCheckId(IdUtil.simpleUUID());
            getResp.setSecretKey(AesUtils.encrypt(point.getSecretKey(), CommonConstant.DEFAULT_AES_KEY, CommonConstant.DEFAULT_AES_IV));

            //将坐标信息存入redis中
            String checkKey = String.format(CaptchaConstant.CAPTCHA_CHECK_KEY, getResp.getCheckId());
            captchaRedisService.set(checkKey, JsonUtils.toJson(point), CaptchaConstant.EXPIRE_CHECK_DURATION);
        } catch (Exception e) {
            log.error("Cut block puzzle image error: ", e);
        }
    }

    /**
     * 随机生成拼图坐标
     */
    private CaptchaPoint generateCaptchaPoint(int backgroundWidth, int backgroundHeight, int slideBlockWidth, int slideBlockHeight) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int widthDiff = backgroundWidth - slideBlockWidth;
        int heightDiff = backgroundHeight - slideBlockHeight;
        int x, y;
        if (widthDiff <= 0) {
            x = 5;
        } else {
            x = random.nextInt(backgroundWidth - slideBlockWidth - 100) + 100;
        }
        if (heightDiff <= 0) {
            y = 5;
        } else {
            y = random.nextInt(backgroundHeight - slideBlockHeight) + 5;
        }

        String aesKey = RandomUtil.randomString(16);
        String aesIv = RandomUtil.randomString(16);
        String secretKey = aesKey + CommonConstant.PIPE + aesIv;

        return new CaptchaPoint(secretKey, x, y);
    }

    /**
     * @param oriImage      原图
     * @param templateImage 模板图
     * @param newImage      新抠出的小图
     * @param x             随机扣取坐标X
     * @param y             随机扣取坐标y
     */
    private static void cutByTemplate(BufferedImage oriImage, BufferedImage templateImage, BufferedImage newImage, int x, int y) {
        //临时数组遍历用于高斯模糊存周边像素值
        int[][] matrix = new int[3][3];
        int[] values = new int[9];

        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();
        // 模板图像宽度
        for (int i = 0; i < xLength; i++) {
            // 模板图片高度
            for (int j = 0; j < yLength; j++) {
                // 如果模板图像当前像素点不是透明色 copy源文件信息到目标图片中
                int rgb = templateImage.getRGB(i, j);
                if (rgb < 0) {
                    newImage.setRGB(i, j, oriImage.getRGB(x + i, y + j));

                    //抠图区域高斯模糊
                    readPixel(oriImage, x + i, y + j, values);
                    fillMatrix(matrix, values);
                    oriImage.setRGB(x + i, y + j, avgMatrix(matrix));
                }

                //防止数组越界判断
                if (i == (xLength - 1) || j == (yLength - 1)) {
                    continue;
                }
                int rightRgb = templateImage.getRGB(i + 1, j);
                int downRgb = templateImage.getRGB(i, j + 1);
                //描边处理，,取带像素和无像素的界点，判断该点是不是临界轮廓点,如果是设置该坐标像素是白色
                if ((rgb >= 0 && rightRgb < 0) || (rgb < 0 && rightRgb >= 0) || (rgb >= 0 && downRgb < 0) || (rgb < 0 && downRgb >= 0)) {
                    newImage.setRGB(i, j, Color.white.getRGB());
                    oriImage.setRGB(x + i, y + j, Color.white.getRGB());
                }
            }
        }

    }


    /**
     * 干扰抠图处理
     *
     * @param oriImage      原图
     * @param templateImage 模板图
     * @param x             随机扣取坐标X
     * @param y             随机扣取坐标y
     */
    private static void interferenceByTemplate(BufferedImage oriImage, BufferedImage templateImage, int x, int y) {
        //临时数组遍历用于高斯模糊存周边像素值
        int[][] matrix = new int[3][3];
        int[] values = new int[9];

        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();
        // 模板图像宽度
        for (int i = 0; i < xLength; i++) {
            // 模板图片高度
            for (int j = 0; j < yLength; j++) {
                // 如果模板图像当前像素点不是透明色 copy源文件信息到目标图片中
                int rgb = templateImage.getRGB(i, j);
                if (rgb < 0) {
                    //抠图区域高斯模糊
                    readPixel(oriImage, x + i, y + j, values);
                    fillMatrix(matrix, values);
                    oriImage.setRGB(x + i, y + j, avgMatrix(matrix));
                }
                //防止数组越界判断
                if (i == (xLength - 1) || j == (yLength - 1)) {
                    continue;
                }
                int rightRgb = templateImage.getRGB(i + 1, j);
                int downRgb = templateImage.getRGB(i, j + 1);
                //描边处理，,取带像素和无像素的界点，判断该点是不是临界轮廓点,如果是设置该坐标像素是白色
                if ((rgb >= 0 && rightRgb < 0) || (rgb < 0 && rightRgb >= 0) || (rgb >= 0 && downRgb < 0) || (rgb < 0 && downRgb >= 0)) {
                    oriImage.setRGB(x + i, y + j, Color.white.getRGB());
                }
            }
        }

    }

    private static void readPixel(BufferedImage img, int x, int y, int[] pixels) {
        int xStart = x - 1;
        int yStart = y - 1;
        int current = 0;
        for (int i = xStart; i < 3 + xStart; i++) {
            for (int j = yStart; j < 3 + yStart; j++) {
                int tx = i;
                if (tx < 0) {
                    tx = -tx;

                } else if (tx >= img.getWidth()) {
                    tx = x;
                }
                int ty = j;
                if (ty < 0) {
                    ty = -ty;
                } else if (ty >= img.getHeight()) {
                    ty = y;
                }
                pixels[current++] = img.getRGB(tx, ty);

            }
        }
    }

    private static void fillMatrix(int[][] matrix, int[] values) {
        int filled = 0;
        for (int[] x : matrix) {
            for (int j = 0; j < x.length; j++) {
                x[j] = values[filled++];
            }
        }
    }

    private static int avgMatrix(int[][] matrix) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int[] x : matrix) {
            for (int j = 0; j < x.length; j++) {
                if (j == 1) {
                    continue;
                }
                Color c = new Color(x[j]);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        return new Color(r / 8, g / 8, b / 8).getRGB();
    }


}
