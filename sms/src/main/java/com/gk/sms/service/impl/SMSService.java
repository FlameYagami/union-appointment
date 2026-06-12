package com.gk.sms.service.impl;

import com.gk.sms.api.SMSApi;
import com.gk.sms.bean.SMSInfo;
import com.gk.sms.bean.SMSResponse;
import com.gk.sms.constant.SMSConstant;
import com.gk.sms.service.intf.ISMSService;
import com.gk.sms.utils.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import javax.annotation.Resource;

@Slf4j
@Service
public class SMSService implements ISMSService {

    @Resource
    private SMSInfo smsInfo;

    @Override
    public boolean sendSMS(String mobile, String message) {
        String ts = String.valueOf(System.currentTimeMillis());
        String sign = smsInfo.getUserId() + ts + smsInfo.getApiKey();

        smsInfo.setTs(ts);
        smsInfo.setSign(SMSUtils.MD5(sign));
        smsInfo.setMobile(mobile);
        smsInfo.setContent(message);
        try {
            Response<SMSResponse> response = SMSApi.sendSMS(smsInfo).execute();
            SMSResponse smsResponse = response.body();
            if (null == smsResponse) {
                throw new Exception("SMS response is null");
            }
            if (!SMSConstant.SMS_SUCCESS.equals(smsResponse.getCode())) {
                log.error("Send SMS failed: mobile({}), status({})", mobile, smsResponse.getMsg());
                return false;
            }
            log.info("Send SMS success: mobile({})", mobile);
            return true;
        } catch (Exception e) {
            log.error("Send SMS error: {}", e.getMessage());
            return false;
        }
    }
}
