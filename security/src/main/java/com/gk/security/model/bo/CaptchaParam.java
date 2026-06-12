package com.gk.security.model.bo;

import com.gk.security.enums.CaptchaType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
@ConfigurationProperties(prefix = "captcha")
@Data
public class CaptchaParam {

    /**
     * 验证码类型
     */
    private String captchaType = CaptchaType.BLOCK_PUZZLE.getValue();

    /**
     * 滑动拼图底图路径
     */
    private String blockPuzzlePath = "";

    /**
     * 点选文字底图路径
     */
    private String clickWordPath = "";

    /**
     * 右下角水印文字(我的水印)
     */
    private String watermark = "";

    /**
     * 右下角水印字体(文泉驿正黑)
     */
    private String watermarkFont = "WenQuanZhengHei.ttf";

    /**
     * 点选文字验证码的文字字体(文泉驿正黑)
     */
    private String fontType = "WenQuanZhengHei.ttf";

    /**
     * 点选字体样式
     */
    private int fontStyle = Font.BOLD;

    /**
     * 点选字体大小
     */
    private int fontSize = 25;

    /**
     * 校验滑动拼图允许误差偏移量(默认5像素)
     */
    private int slipOffset = 5;

    /**
     * 滑块干扰项(0/1/2)
     */
    private int interferenceOptions = 0;

    /**
     * 一分钟内接口请求次数限制 开关
     */
    private boolean reqLimit = true;

    /***
     * 一分钟内get接口失败次数将锁定
     */
    private int reqGetLockLimit = 10;

    /**
     * get接口锁定时间(秒)
     */
    private int reqGetLockDuration = 300;

    /***
     * get接口一分钟内限制访问数
     */
    private int reqGetMinuteLimit = 20;
    /***
     * check接口一分钟内限制访问数
     */
    private int reqCheckMinuteLimit = 20;
    /***
     * verify接口一分钟内限制访问数
     */
    private int reqVerifyMinuteLimit = 20;

    /**
     * 点选文字个数
     */
    private int clickWordCount = 4;

}
