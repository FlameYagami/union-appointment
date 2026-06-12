package com.gk.callapi.demo.api;

import com.gk.callapi.demo.model.DemoGetResp;
import com.gk.callapi.demo.model.DemoResp;
import com.gk.common.enums.SysStatus;
import com.gk.common.retrofit.service.BaseApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import javax.annotation.PostConstruct;

/**
 * 示例请求服务
 *
 * @author Flame
 * @date 2023-04-23 16:18
 **/
@Component
@Slf4j
public class DemoApi extends BaseApi {

    /**
     * 示例: 基础请求地址
     */
    @Value("${demo-api.baseUrl}")
    private String baseUrl;

    private IDemoApi demoApi;

    @PostConstruct
    public void lateInit() {
        demoApi = createApiService(baseUrl, IDemoApi.class);
    }

    /**
     * 将Call转换为data或者返回null
     *
     * @param <T> 响应的数据
     */
    private <T> T callToDataOrNull(Call<DemoResp<T>> call) {
        try {
            Response<DemoResp<T>> response = call.execute();
            DemoResp<T> body = response.body();
            if (null == body) {
                log.error("Api resp is null");
                return null;
            }
            // 判定code值
            if (SysStatus.SUCCESS.getCode() != body.getCode()) {
                log.error("Api code({}) error: message({})", body.getCode(), body.getMessage());
                return null;
            }
            return body.getData();
        } catch (Exception e) {
            log.error("Api execute error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 示例: Get请求
     */
    public DemoGetResp getDemo(String propertyA, int propertyB) {
        return callToDataOrNull(demoApi.getDemo(propertyA, propertyB));
    }

    /**
     * 示例: Get请求(异步)
     */
    public Call<DemoResp<DemoGetResp>> getDemoAsync(String propertyA, int propertyB) {
        return demoApi.getDemo(propertyA, propertyB);
    }


}
