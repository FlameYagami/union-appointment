package com.gk.common.utils;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Gson封装工具类
 *
 * @author Flame
 * @date 2020/03/20
 **/
@Slf4j
public class JsonUtils {

    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 序列化
     *
     * @param obj 对象
     * @return Json
     */
    public static String toJson(Object obj) {
        return new GsonBuilder().serializeNulls().create().toJson(obj);
    }

    /**
     * 反序列化对象
     *
     * @param data Json
     * @return 对象
     */
    public static <T> T toObject(String data, Class<T> classType) {
        try {
            return new Gson().fromJson(data, classType);
        } catch (Exception e) {
            log.error("Gson to Object Error: json string = {}, class type = {}", data, classType.getName());
            return null;
        }
    }

    /**
     * 反序列化数组对象
     *
     * @param data Json
     * @return 数组集合
     */
    public static <T> List<T> toArray(String data, Class<T[]> clazz) {
        try {
            Gson gson = new Gson();
            T[] array = gson.fromJson(data, clazz);
            return Arrays.asList(array);
        } catch (Exception e) {
            log.error("Gson to Array Error: json string = {}, class type = {}", data, clazz.getName());
            return new ArrayList<>();
        }
    }

    /**
     * 反序列化集合对象
     *
     * @param data Json
     * @return 对象集合
     */
    public static <T> List<T> toList(String data, Class<T> classType) {
        List<T> arrayList = new ArrayList<>();
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            Gson gson = new Gson();
            List<JsonObject> jsonObjects = gson.fromJson(data, type);
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(gson.fromJson(jsonObject, classType));
            }
            return arrayList;
        } catch (Exception e) {
            log.error("Gson to List Error: json string = {}, class type = {}", data, classType.getName());
            return arrayList;
        }
    }

    /**
     * 将 JsonObject 对象转换为 Object 对象
     *
     * @param obj JsonElement
     * @return 对象
     */
    public static Object toObject(JsonElement obj) {
        try {
            return new Gson().fromJson(obj, Object.class);
        } catch (Exception e) {
            log.error("Gson to Object Error: json element string = {}", JsonUtils.toJson(obj));
            return null;
        }
    }

    /**
     * 将 Object 对象转换为 JsonObject 对象
     *
     * @param obj Object
     * @return JsonObject
     */
    public static JsonObject toJsonObject(Object obj) {
        try {
            Gson gson = new GsonBuilder()
                    .serializeNulls()  // 包括 null 值属性
                    .create();
            return gson.toJsonTree(obj).getAsJsonObject();
        } catch (Exception e) {
            log.error("Gson to JsonObject Error: object string = {}", toJson(obj));
            return null;
        }
    }


}
