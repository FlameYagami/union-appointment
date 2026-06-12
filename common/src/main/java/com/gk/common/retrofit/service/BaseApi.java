package com.gk.common.retrofit.service;

import com.gk.common.retrofit.config.ApiConfig;
import com.gk.common.retrofit.interceptor.LogInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * API抽象类
 *
 * @author Flame
 * @date 2020/03/25
 **/
public abstract class BaseApi {

    /**
     * 默认OkHttp客户端
     */
    protected static class DefaultOkHttpClientHolder {
        public static OkHttpClient instance = new OkHttpClient.Builder()
                .connectTimeout(ApiConfig.DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(ApiConfig.DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(ApiConfig.DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new LogInterceptor())
                .build();
    }

    /**
     * 创建Retrofit实例(使用默认的OkHttp客户端)
     */
    protected static <T> T createApiService(String baseUrl, Class<T> tClass) {
        return new Retrofit.Builder()
                .client(DefaultOkHttpClientHolder.instance)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
                .create(tClass);
    }

}
