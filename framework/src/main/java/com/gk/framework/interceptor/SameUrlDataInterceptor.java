package com.gk.framework.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.annotation.RepeatSubmit;
import com.gk.framework.constant.RedisConstant;
import com.gk.framework.filter.AbstractServletRequestWrapper;
import com.gk.framework.manager.RedisManager;
import com.gk.framework.utils.TokenUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 判断请求数据是否和上一次相同
 * 如果和上次相同，则是重复提交表单.有效时间为5秒内。
 *
 * @author Flame
 * @since 2025-07-23 10:16
 */
@Component
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor {

    /**
     * 请求时间
     */
    public static final String REQUEST_TIME = "requestTime";

    /**
     * 数据签名
     */
    public static final String DATA_MD5 = "dataMd5";

    @Resource
    private RedisManager redisManager;

    @SuppressWarnings("unchecked")
    @Override
    public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation) {
        String nowParams = CommonConstant.EMPTY;
        if (request instanceof AbstractServletRequestWrapper) {
            AbstractServletRequestWrapper wrapper = (AbstractServletRequestWrapper) request;
            nowParams = wrapper.getBodyString();
        }

        // body参数为空，获取Parameter的数据
        if (StrUtil.isEmpty(nowParams)) {
            nowParams = JsonUtils.toJson(request.getParameterMap());
        }

        // 将请求数据转换为md5签名
        String dataMd5 = DigestUtil.md5Hex(nowParams);
        Map<String, Object> nowDataMap = new HashMap<>() {{
            put(DATA_MD5, dataMd5);
            put(REQUEST_TIME, System.currentTimeMillis());
        }};

        // 查询令牌
        String token = TokenUtils.getRequestToken(request);
        if (StrUtil.isEmpty(token)) {
            // 查询不到设置为匿名
            token = "anonymous";
        }

        // 构建请求标志Key(repeat_submit:{token}:{uri}:{httpMethod})
        String dataKey = StrUtil.format("{}{}:{}:{}", RedisConstant.REPEAT_SUBMIT_KEY, token, request.getRequestURI(), request.getMethod());
        // 缓存获取上一次的请求数据
        Map<String, Object> preDataMap = redisManager.getHashEntries(dataKey);
        // 缓存对比请求数据
        if (null != preDataMap && compareDataMd5(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap, annotation.interval())) {
            return true;
        }

        // 缓存请求数据并设置过期时间
        redisManager.hashPutAll(dataKey, nowDataMap, annotation.interval(), TimeUnit.MILLISECONDS);
        return false;
    }

    /**
     * 判断数据签名是否相同
     */
    private boolean compareDataMd5(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(DATA_MD5);
        String preParams = (String) preMap.get(DATA_MD5);
        return nowParams.equals(preParams);
    }

    /**
     * 判断两次间隔时间
     */
    private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap, int interval) {
        long time1 = (Long) nowMap.get(REQUEST_TIME);
        long time2 = (Long) preMap.get(REQUEST_TIME);
        return (time1 - time2) < interval;
    }
}
