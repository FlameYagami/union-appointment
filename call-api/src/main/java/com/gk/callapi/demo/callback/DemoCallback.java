package com.gk.callapi.demo.callback;

import com.gk.callapi.demo.model.DemoResp;
import com.gk.common.enums.SysStatus;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 示例异步回调拆包类
 *
 * @author Flame
 * @date 2023-04-26 14:46
 **/
@Slf4j
public abstract class DemoCallback<T> implements Callback<DemoResp<T>> {

    /**
     * 响应回调
     *
     * @param data 响应对象
     */
    public abstract void success(T data);

    /**
     * 失败回调
     *
     * @param throwable 抛出异常
     */
    public abstract void failure(Throwable throwable);

    @Override
    public void onResponse(@NotNull Call<DemoResp<T>> call, Response<DemoResp<T>> response) {
        DemoResp<T> body = response.body();
        if (null == body) {
            log.error("Api resp is null");
            failure(new Throwable("Api resp is null"));
            return;
        }
        // 判定code值, 决定调用成功还是失败接口
        if (SysStatus.SUCCESS.getCode() != body.getCode()) {
            log.error("Api code({}) error: message({})", body.getCode(), body.getMessage());
            failure(new Throwable(body.getMessage()));
            return;
        }
        success(body.getData());
    }

    @Override
    public void onFailure(@NotNull Call<DemoResp<T>> call, @NotNull Throwable t) {
        failure(t);
    }

}
