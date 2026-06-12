package com.gk.sms.api;

import com.gk.common.retrofit.service.BaseApi;
import com.gk.sms.bean.SMSInfo;
import com.gk.sms.bean.SMSResponse;

import retrofit2.Call;

/**
 * Created by Flame on 2020/03/25.
 **/
public class SMSApi extends BaseApi {

    public static class SMSHolder {
        static ISMS apiService = createApiService(ISMS.baseUrl, ISMS.class);
    }

    public static Call<SMSResponse> sendSMS(SMSInfo smsInfo) {
        return SMSHolder.apiService.sendSMS(
                smsInfo.getUserId(),
                smsInfo.getTs(),
                smsInfo.getSign(),
                smsInfo.getMobile(),
                smsInfo.getContent()
        );
    }

}
