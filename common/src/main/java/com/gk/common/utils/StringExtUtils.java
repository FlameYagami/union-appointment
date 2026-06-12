package com.gk.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.common.constant.DateConstant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 字符串扩展工具类
 *
 * @author Kevin
 * @date 2020-03-10 11:32
 **/

public class StringExtUtils {

    private StringExtUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 十六进制字符串转为Byte数组
     */
    public static byte[] toByteArray(String hexString) {
        if (StrUtil.isEmpty(hexString)) {
            return new byte[0];
        }
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index > hexString.length() - 1) {
                return byteArray;
            }
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }

    /**
     * Byte数组转为十六进制字符串
     */
    public static String toHexString(byte[] byteArray) {
        final StringBuilder hexString = new StringBuilder();
        if (byteArray == null || byteArray.length == 0) {
            return null;
        }
        for (byte b : byteArray) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        return hexString.toString().toLowerCase();
    }

    /**
     * 随机数字
     *
     * @param bound 范围
     */
    public static int randomNumber(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    /**
     * 随机数字
     *
     * @param min 最小值
     * @param max 最大值
     * @return 数字
     */
    public static int randomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
    }

    /**
     * 随机数字字符串
     *
     * @param min 最小值
     * @param max 最大值
     * @return 数字字符串
     */
    public static String randomNumberString(int min, int max) {
        return String.valueOf(randomNumber(min, max));
    }

    /**
     * 逗号分割
     */
    public static List<Long> splitComma(String content) {
        if (StrUtil.isEmpty(content)) {
            return new ArrayList<>();
        }
        return Arrays.stream(content.split(CommonConstant.COMMA))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * 符号拆分
     *
     * @param splitString 拆分字符
     * @param content 内容字符串
     * @return 字符串集合
     */
    public static List<String> split(String splitString, String content) {
        if (StrUtil.isEmpty(content)) {
            return new ArrayList<>();
        }
        return List.of(content.split(splitString));
    }

    /**
     * 逗号分隔
     */
    public static List<String> splitCommaStr(String content) {
        return split(CommonConstant.COMMA, content);
    }

    /**
     * 符号拼接
     *
     * @param joinCharacter 拼接字符
     * @param contentList 需要拼接的内容集合
     */
    public static <T> String join(CharSequence joinCharacter, List<T> contentList) {
        if (CollUtil.isEmpty(contentList)) {
            return CommonConstant.EMPTY;
        }
        return contentList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(joinCharacter));
    }

    /**
     * 逗号拼接
     *
     * @param contentList 需要拼接的内容集合
     */
    public static <T> String joinComma(List<T> contentList) {
        return join(CommonConstant.COMMA, contentList);
    }

    /**
     * 逗号空格拼接
     *
     * @param contentList 需要拼接的内容集合
     */
    public static <T> String joinCommaWithSpace(List<T> contentList) {
        return join(CommonConstant.COMMA + CommonConstant.SPACE, contentList);
    }

    /**
     * 包裹字符串
     *
     * @param content 被包裹的内容
     * @param packSymbol 包裹符号
     */
    public static String pack(String content, String packSymbol) {
        return packSymbol + content + packSymbol;
    }

    /**
     * 所有类型转换成字符串, 时间类型转换为标准日期字符串
     *
     * @param value 任意类型的值
     */
    public static Object formatObjectDate(Object value) {
        return formatObjectDate(value, DateConstant.DEFAULT_PATTERN);
    }

    /**
     * 所有类型转换成字符串, 时间类型按照时间格式转换
     *
     * @param value 任意类型的值
     * @param datePattern 时间格式
     */
    public static Object formatObjectDate(Object value, String datePattern) {
        if (null == value) {
            return null;
        }
        if (value instanceof LocalDateTime) {
            return DateUtil.format((LocalDateTime) value, datePattern);
        }
        if (value instanceof Date) {
            return DateUtil.format((Date) value, datePattern);
        }
        return value.toString();
    }

    /**
     * 隐藏超管账号
     */
    public static String hideSuperAdmin(String value) {
        if (value.equals(CommonConstant.SUPER_ADMIN_USERNAME)) {
            return CommonConstant.EMPTY;
        }
        return value;
    }

    /**
     * 隐藏特殊账号
     */
    public static String hideSpecial(String value) {
        if (value.equals(CommonConstant.SUPER_ADMIN_USERNAME) || value.equals(CommonConstant.ANONYMOUS_USERNAME) || value.equals(CommonConstant.PEOPLE_USERNAME)
        ) {
            return CommonConstant.EMPTY;
        }
        return value;
    }

}
