package com.gk.security.service.impl;

import cn.hutool.core.collection.CollUtil;
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
import com.gk.security.model.dto.captcha.CaptchaCheckReq;
import com.gk.security.model.dto.captcha.ClickWordGetResp;
import com.gk.security.model.dto.captcha.base.BaseCaptchaGetResp;
import com.gk.security.service.intf.CaptchaCacheService;
import com.gk.security.service.intf.CaptchaService;
import com.gk.security.utils.CaptchaImageUtils;
import com.gk.security.utils.CaptchaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 点选文字验证码 业务实现类
 * <p>
 * @author Kevin
 * @date 2023-08-11 15:06
 */
@Service
@Slf4j
public class ClickWordCaptchaServiceImpl implements CaptchaService {

    @Resource
    private CaptchaParam captchaParam;

    @Resource
    private CaptchaLimitHandler captchaLimitHandler;

    @Resource
    private CaptchaCacheService captchaRedisService;

    public static String HAN_ZI = "\u7684\u4e00\u4e86\u662f\u6211\u4e0d\u5728\u4eba\u4eec\u6709\u6765\u4ed6\u8fd9\u4e0a\u7740\u4e2a\u5730\u5230\u5927\u91cc\u8bf4\u5c31\u53bb\u5b50\u5f97\u4e5f\u548c\u90a3\u8981\u4e0b\u770b\u5929\u65f6\u8fc7\u51fa\u5c0f\u4e48\u8d77\u4f60\u90fd\u628a\u597d\u8fd8\u591a\u6ca1\u4e3a\u53c8\u53ef\u5bb6\u5b66\u53ea\u4ee5\u4e3b\u4f1a\u6837\u5e74\u60f3\u751f\u540c\u8001\u4e2d\u5341\u4ece\u81ea\u9762\u524d\u5934\u9053\u5b83\u540e\u7136\u8d70\u5f88\u50cf\u89c1\u4e24\u7528\u5979\u56fd\u52a8\u8fdb\u6210\u56de\u4ec0\u8fb9\u4f5c\u5bf9\u5f00\u800c\u5df1\u4e9b\u73b0\u5c71\u6c11\u5019\u7ecf\u53d1\u5de5\u5411\u4e8b\u547d\u7ed9\u957f\u6c34\u51e0\u4e49\u4e09\u58f0\u4e8e\u9ad8\u624b\u77e5\u7406\u773c\u5fd7\u70b9\u5fc3\u6218\u4e8c\u95ee\u4f46\u8eab\u65b9\u5b9e\u5403\u505a\u53eb\u5f53\u4f4f\u542c\u9769\u6253\u5462\u771f\u5168\u624d\u56db\u5df2\u6240\u654c\u4e4b\u6700\u5149\u4ea7\u60c5\u8def\u5206\u603b\u6761\u767d\u8bdd\u4e1c\u5e2d\u6b21\u4eb2\u5982\u88ab\u82b1\u53e3\u653e\u513f\u5e38\u6c14\u4e94\u7b2c\u4f7f\u5199\u519b\u5427\u6587\u8fd0\u518d\u679c\u600e\u5b9a\u8bb8\u5feb\u660e\u884c\u56e0\u522b\u98de\u5916\u6811\u7269\u6d3b\u90e8\u95e8\u65e0\u5f80\u8239\u671b\u65b0\u5e26\u961f\u5148\u529b\u5b8c\u5374\u7ad9\u4ee3\u5458\u673a\u66f4\u4e5d\u60a8\u6bcf\u98ce\u7ea7\u8ddf\u7b11\u554a\u5b69\u4e07\u5c11\u76f4\u610f\u591c\u6bd4\u9636\u8fde\u8f66\u91cd\u4fbf\u6597\u9a6c\u54ea\u5316\u592a\u6307\u53d8\u793e\u4f3c\u58eb\u8005\u5e72\u77f3\u6ee1\u65e5\u51b3\u767e\u539f\u62ff\u7fa4\u7a76\u5404\u516d\u672c\u601d\u89e3\u7acb\u6cb3\u6751\u516b\u96be\u65e9\u8bba\u5417\u6839\u5171\u8ba9\u76f8\u7814\u4eca\u5176\u4e66\u5750\u63a5\u5e94\u5173\u4fe1\u89c9\u6b65\u53cd\u5904\u8bb0\u5c06\u5343\u627e\u4e89\u9886\u6216\u5e08\u7ed3\u5757\u8dd1\u8c01\u8349\u8d8a\u5b57\u52a0\u811a\u7d27\u7231\u7b49\u4e60\u9635\u6015\u6708\u9752\u534a\u706b\u6cd5\u9898\u5efa\u8d76\u4f4d\u5531\u6d77\u4e03\u5973\u4efb\u4ef6\u611f\u51c6\u5f20\u56e2\u5c4b\u79bb\u8272\u8138\u7247\u79d1\u5012\u775b\u5229\u4e16\u521a\u4e14\u7531\u9001\u5207\u661f\u5bfc\u665a\u8868\u591f\u6574\u8ba4\u54cd\u96ea\u6d41\u672a\u573a\u8be5\u5e76\u5e95\u6df1\u523b\u5e73\u4f1f\u5fd9\u63d0\u786e\u8fd1\u4eae\u8f7b\u8bb2\u519c\u53e4\u9ed1\u544a\u754c\u62c9\u540d\u5440\u571f\u6e05\u9633\u7167\u529e\u53f2\u6539\u5386\u8f6c\u753b\u9020\u5634\u6b64\u6cbb\u5317\u5fc5\u670d\u96e8\u7a7f\u5185\u8bc6\u9a8c\u4f20\u4e1a\u83dc\u722c\u7761\u5174\u5f62\u91cf\u54b1\u89c2\u82e6\u4f53\u4f17\u901a\u51b2\u5408\u7834\u53cb\u5ea6\u672f\u996d\u516c\u65c1\u623f\u6781\u5357\u67aa\u8bfb\u6c99\u5c81\u7ebf\u91ce\u575a\u7a7a\u6536\u7b97\u81f3\u653f\u57ce\u52b3\u843d\u94b1\u7279\u56f4\u5f1f\u80dc\u6559\u70ed\u5c55\u5305\u6b4c\u7c7b\u6e10\u5f3a\u6570\u4e61\u547c\u6027\u97f3\u7b54\u54e5\u9645\u65e7\u795e\u5ea7\u7ae0\u5e2e\u5566\u53d7\u7cfb\u4ee4\u8df3\u975e\u4f55\u725b\u53d6\u5165\u5cb8\u6562\u6389\u5ffd\u79cd\u88c5\u9876\u6025\u6797\u505c\u606f\u53e5\u533a\u8863\u822c\u62a5\u53f6\u538b\u6162\u53d4\u80cc\u7ec6";

    /**
     * 点选文字 字体颜色是否随机
     */
    private boolean fontColorRandom = true;

    @Override
    public BaseCaptchaGetResp get(CaptchaInfo captchaInfo) {
        ClickWordGetResp getResp = (ClickWordGetResp)CaptchaUtils.createGetReq(captchaParam.getCaptchaType());
        if (captchaParam.isReqLimit()) {
            captchaLimitHandler.validateGet(captchaInfo.getClientUid());
        }

        BufferedImage backgroundImage = CaptchaImageUtils.getClickWordImage();
        if (null == backgroundImage) {
            log.error("点选文字底图初始化失败");
            throw new SysException(SysStatus.CAPTCHA_IMAGE_ERROR);
        }
        getImageData(getResp, backgroundImage);
        if (StrUtil.isEmpty(getResp.getBackgroundImage())) {
            log.error("点选文字验证码图片处理失败");
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
        if (!captchaRedisService.exists(checkKey)) {
            throw new SysException(SysStatus.CAPTCHA_EXPIRE_ERROR);
        }
        // 验证码只用一次，即刻失效
        captchaRedisService.delete(checkKey);

        List<CaptchaPoint> pointList = JsonUtils.toList(checkPoint, CaptchaPoint.class);
        if (CollUtil.isEmpty(pointList) || StrUtil.isEmpty(pointList.get(0).getSecretKey())) {
            captchaLimitHandler.afterValidateFail(checkReq.getClientUid());
            throw new SysException(SysStatus.CAPTCHA_EXPIRE_ERROR);
        }
        List<String> secretKeyList = StrUtil.split(pointList.get(0).getSecretKey(), CommonConstant.PIPE);
        if (secretKeyList.size() != CaptchaConstant.SECRET_KEY_SIZE) {
            captchaLimitHandler.afterValidateFail(checkReq.getClientUid());
            throw new SysException(SysStatus.CAPTCHA_CHECK_ERROR);
        }
        String aesKey = secretKeyList.get(0);
        String aesIv = secretKeyList.get(1);
        String reqPointJson = AesUtils.decrypt(checkReq.getPointJson(), aesKey, aesIv);
        List<CaptchaPoint> reqPointList = JsonUtils.toList(reqPointJson, CaptchaPoint.class);
        if (CollUtil.isEmpty(reqPointList)) {
            captchaLimitHandler.afterValidateFail(checkReq.getClientUid());
            throw new SysException(SysStatus.CAPTCHA_CHECK_ERROR);
        }

        for (int i = 0; i < pointList.size(); i++) {
            if (pointList.get(i).getX() - CaptchaConstant.HAN_ZI_SIZE > reqPointList.get(i).getX()
                    || reqPointList.get(i).getX() > pointList.get(i).getX() + CaptchaConstant.HAN_ZI_SIZE
                    || pointList.get(i).getY() - CaptchaConstant.HAN_ZI_SIZE > reqPointList.get(i).getY()
                    || reqPointList.get(i).getY() > pointList.get(i).getY() + CaptchaConstant.HAN_ZI_SIZE) {
                captchaLimitHandler.afterValidateFail(checkReq.getClientUid());
                throw new SysException(SysStatus.CAPTCHA_CHECK_ERROR);
            }
        }
        //校验成功，将信息存入缓存
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

    private void getImageData(ClickWordGetResp getResp, BufferedImage backgroundImage) {
        List<String> wordList = new ArrayList<>();
        List<CaptchaPoint> pointList = new ArrayList<>();

        Graphics backgroundGraphics = backgroundImage.getGraphics();
        int width = backgroundImage.getWidth();
        int height = backgroundImage.getHeight();

        int wordCount = captchaParam.getClickWordCount();
        //定义随机1到arr.length某一个字不参与校验
        int num = StringExtUtils.randomNumber(1, wordCount);
        Set<String> currentWords = getRandomWords(wordCount);

        String aesKey = RandomUtil.randomString(16);
        String aesIv = RandomUtil.randomString(16);
        String secretKey = aesKey + CommonConstant.PIPE + aesIv;

        Font clickWordFont = CaptchaUtils.loadFont(captchaParam.getFontType(), captchaParam.getFontStyle(), captchaParam.getFontSize());

        int i = 0;
        for (String word : currentWords) {
            //随机字体坐标
            CaptchaPoint point = randomWordPoint(width, height, i, wordCount);
            point.setSecretKey(secretKey);
            //随机字体颜色
            if (this.fontColorRandom) {
                backgroundGraphics.setColor(new Color(StringExtUtils.randomNumber(1, 255),
                        StringExtUtils.randomNumber(1, 255), StringExtUtils.randomNumber(1, 255)));
            } else {
                backgroundGraphics.setColor(Color.BLACK);
            }
            //设置角度
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians(StringExtUtils.randomNumber(-45, 45)), 0, 0);
            Font rotatedFont = clickWordFont.deriveFont(affineTransform);
            backgroundGraphics.setFont(rotatedFont);
            backgroundGraphics.drawString(word, point.getX(), point.getY());

            if ((num - 1) != i) {
                wordList.add(word);
                pointList.add(point);
            }
            i++;
        }

        CaptchaImageUtils.setupWatermark(backgroundImage, captchaParam.getWatermark(), captchaParam.getFontStyle(), captchaParam.getWatermarkFont());

        //创建合并图片
        BufferedImage combinedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics combinedGraphics = combinedImage.getGraphics();
        combinedGraphics.drawImage(backgroundImage, 0, 0, null);

        getResp.setBackgroundImage(CaptchaImageUtils.getImageBase64Str(backgroundImage).replaceAll("[\r\n]", ""));
        getResp.setWordList(wordList);
        getResp.setCheckId(IdUtil.simpleUUID());
        getResp.setSecretKey(AesUtils.encrypt(secretKey, CommonConstant.DEFAULT_AES_KEY, CommonConstant.DEFAULT_AES_IV));
        //将坐标信息存入redis中
        String checkKey = String.format(CaptchaConstant.CAPTCHA_CHECK_KEY, getResp.getCheckId());
        captchaRedisService.set(checkKey, JsonUtils.toJson(pointList), CaptchaConstant.EXPIRE_CHECK_DURATION);
    }

    private Set<String> getRandomWords(int wordCount) {
        Set<String> words = new HashSet<>();
        int size = HAN_ZI.length();
        while (words.size() < wordCount) {
            String t = HAN_ZI.charAt(StringExtUtils.randomNumber(size)) + "";
            words.add(t);
        }
        return words;
    }

    /**
     * 随机字体循环排序下标
     *
     * @param imageWidth    图片宽度
     * @param imageHeight   图片高度
     * @param wordSortIndex 字体循环排序下标(i)
     * @param wordCount     字数量
     * @return
     */
    private static CaptchaPoint randomWordPoint(int imageWidth, int imageHeight, int wordSortIndex, int wordCount) {
        int avgWidth = imageWidth / (wordCount + 1);
        int x, y;
        if (avgWidth < CaptchaConstant.HAN_ZI_HALF_SIZE) {
            x = StringExtUtils.randomNumber(1 + CaptchaConstant.HAN_ZI_HALF_SIZE, imageWidth);
        } else {
            if (wordSortIndex == 0) {
                x = StringExtUtils.randomNumber(1 + CaptchaConstant.HAN_ZI_HALF_SIZE, avgWidth * (wordSortIndex + 1) - CaptchaConstant.HAN_ZI_HALF_SIZE);
            } else {
                x = StringExtUtils.randomNumber(avgWidth * wordSortIndex + CaptchaConstant.HAN_ZI_HALF_SIZE, avgWidth * (wordSortIndex + 1) - CaptchaConstant.HAN_ZI_HALF_SIZE);
            }
        }
        y = StringExtUtils.randomNumber(CaptchaConstant.HAN_ZI_SIZE, imageHeight - CaptchaConstant.HAN_ZI_SIZE);
        return new CaptchaPoint(null, x, y);
    }


}
