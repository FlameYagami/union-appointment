package com.gk.common.utils;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gk.common.constant.DateConstant;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Jackson封装工具类
 *
 * @author GuoYu
 * @since 2022-10-08 16:15
 **/

@Slf4j
public class JacksonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private JacksonUtils() {
        throw new IllegalStateException("Utility class");
    }

    static {
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat(DateConstant.DEFAULT_PATTERN));
        objectMapper.setTimeZone(TimeZone.getTimeZone(DateConstant.DEFAULT_TIME_ZONE));
        // 注册一个时间序列化及反序列化的处理模块，用于解决jdk8中localDateTime等的序列化问题
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * 初始化 objectMapper 属性
     * <p>
     * 通过这样的方式，使用 Spring 创建的 ObjectMapper Bean
     *
     * @param objectMapper ObjectMapper 对象
     */
    public static void init(ObjectMapper objectMapper) {
        JacksonUtils.objectMapper = objectMapper;
    }

    /**
     * 序列化
     */
    public static String toJson(Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Jackson Json to String Error: object = {}", obj);
        }
        return null;
    }

    /**
     * 反序列化
     */
    public static <T> T parseJson(String json, Class<T> valueType) {
        if (!StrUtil.isEmpty(json) && !Objects.isNull(valueType)) {
            try {
                return objectMapper.readValue(json, valueType);
            } catch (Exception e) {
                log.error("Jackson Json Parse Object Error: json string = {}, class type = {}", json, valueType.getName());
            }
        }
        return null;
    }

    /**
     * 反序列化成list
     */
    public static <T> List<T> parseList(String json, Class<T> clazz) {
        if (!StrUtil.isEmpty(json) && !Objects.isNull(clazz)) {
            try {
                return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            } catch (Exception e) {
                log.error("Jackson Json Parse List Error: json string = {}, class type = {}", json, clazz.getName());
            }
        }
        return Collections.emptyList();
    }

    /**
     * 反序列化成map
     */
    public static <K, V> Map<K, V> parseMap(String json, Class<K> kClass, Class<V> vClass) {
        if (!StrUtil.isEmpty(json) && !Objects.isNull(kClass) && !Objects.isNull(vClass)) {
            try {
                return objectMapper.readValue(json, objectMapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
            } catch (Exception e) {
                log.error("Jackson Json Parse Map Error: json string = {}, key type = {}, value type = {}", json, kClass.getName(), vClass.getName());
            }
        }
        return Collections.emptyMap();
    }

}
