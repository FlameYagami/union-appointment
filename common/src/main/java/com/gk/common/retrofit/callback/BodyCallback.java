package com.gk.common.retrofit.callback;

import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 网络请求异步回调, 自动拆包body对象
 *
 * @author Flame
 * @date 2020-03-25
 **/
public abstract class BodyCallback<T> implements Callback<T> {

    /**
     * 响应回调
     *
     * @param response 响应对象
     */
    public abstract void success(T response);

    /**
     * 失败回调
     *
     * @param throwable 抛出异常
     */
    public abstract void failure(Throwable throwable);

    @Override
    public void onResponse(@NotNull Call<T> call, Response<T> response) {
        success(response.body());
    }

    @Override
    public void onFailure(@NotNull Call<T> call, @NotNull Throwable throwable) {
        failure(throwable);
    }

}
