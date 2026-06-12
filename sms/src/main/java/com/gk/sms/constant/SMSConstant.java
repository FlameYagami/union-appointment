package com.gk.sms.constant;

import java.util.HashMap;
import java.util.Map;

public class SMSConstant {

    public static final String SMS_SUCCESS = "0";

    public static final Map<String, String> SMSStatusMaps = new HashMap<String, String>() {
        {
            put("9001", "签名格式不正确");
            put("9002", " 参数未赋值");
            put("9003", " 手机号码格式不正确");
            put("9006", " 用户accesskey不正确");
            put("9007", " IP白名单限制");
            put("9009", " 短信内容参数不正确");
            put("9010", " 用户短信余额不足");
            put("9011", " 用户帐户异常");
            put("9012", " 日期时间格式不正确");
            put("9013", " 不合法的语音验证码，4 ~8 位的数字 ");
            put("9014", " 超出了最大手机号数量 ");
            put("9015", " 不支持的国家短信 ");
            put("9016", " 无效的签名或者签名ID ");
            put("9017", " 无效的模板ID ");
            put("9018", " 单个变量限制为1 - 20个字");
            put("9019", " 内容不可以为空");
            put("9021", " 主叫和被叫号码不能相同");
            put("9022", " 手机号码不能为空");
            put("9023", " 手机号码黑名单");
            put("9024", " 手机号码超频");
            put("10001", " 内容包含敏感词");
            put("10002", " 内容包含屏蔽词");
            put("10003", " 错误的定时时间");
            put("10004", " 自定义扩展只能是数字且长度不能超过4位");
            put("10005", " 模版类型不存在");
            put("10006", " 模版和内容不匹配");
        }
    };
}
