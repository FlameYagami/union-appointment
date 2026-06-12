package com.gk.callapi.demo.api;

import com.gk.callapi.demo.model.DemoGetResp;
import com.gk.callapi.demo.model.DemoPostReq;
import com.gk.callapi.demo.model.DemoPostResp;
import com.gk.callapi.demo.model.DemoResp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.Map;

/**
 * 示例请求接口
 *
 * @author Flame
 * @date 2023-04-23 16:18
 **/
public interface IDemoApi {

    /**
     * 以Url形式的请求
     */
    @GET("demo/call-api/demo")
    Call<DemoResp<DemoGetResp>> getDemo(
            @Query("propertyA") String propertyA,
            @Query("propertyB") int propertyB
    );

    /**
     * 以Url形式的请求
     */
    @GET("demo/call-api/demo")
    Call<DemoResp<DemoGetResp>> getDemo(
            @QueryMap Map<String, Object> params
    );

    /**
     * 以Json形式的请求
     */
    @POST("demo/call-api/demo")
    Call<DemoResp<DemoPostResp>> postDemo(@Body DemoPostReq req);

    /**
     * 以Url形式的请求
     */
    @POST("demo/call-api/demo")
    @FormUrlEncoded
    Call<DemoResp<DemoPostResp>> postDemo(
            @Field("propertyA") String propertyA,
            @Field("propertyB") int propertyB
    );

}
