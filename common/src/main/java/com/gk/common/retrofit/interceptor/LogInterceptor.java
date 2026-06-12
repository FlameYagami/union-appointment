package com.gk.common.retrofit.interceptor;

import com.gk.common.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Http请求日志拦截器
 *
 * @author Flame
 * @date 2020-03-24
 **/
@Slf4j
public class LogInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LogUtils.logCallApi().info("Http request => {}", request);
        Response response = chain.proceed(chain.request());
        ResponseBody responseBody = response.body();
        String content = responseBody.string();
        MediaType mediaType = response.body().contentType();
        LogUtils.logCallApi().info("Http response => {}", content);
        return response.newBuilder()
                .body(ResponseBody.create(mediaType, content))
                .build();
    }
}
