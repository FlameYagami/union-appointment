package com.gk.sms.api;

import com.gk.sms.bean.SMSResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Flame on 2020/03/25.
 **/
public interface ISMS {

    String baseUrl = "http://118.212.186.124:8081/";

    @POST("api/sms/send")
    @FormUrlEncoded
    Call<SMSResponse> sendSMS(
            @Field("userid") String userId,
            @Field("ts") String ts,
            @Field("sign") String sign,
            @Field("mobile") String mobile,
            @Field("msgcontent") String content
    );
}
