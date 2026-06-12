package com.gk.server.filter;

import cn.hutool.core.lang.Pair;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.AesUtils;
import com.gk.common.utils.JsonUtils;
import com.gk.common.utils.ServletExtUtils;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.common.model.others.ApiResult;
import com.gk.server.model.others.EncryptApiResult;
import com.gk.server.model.others.EncryptPageResult;
import com.gk.common.model.others.PageResult;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 接口响应加密封装类
 *
 * @author GuoYu
 * @since 2023-03-09 13:34
 */
@Slf4j
public class ServerRespWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream byteArrayOutputStream;
    private final ServletOutputStream servletOutputStream;

    public ServerRespWrapper(HttpServletResponse response) {
        super(response);
        // 真正存储数据的流
        byteArrayOutputStream = new ByteArrayOutputStream();
        servletOutputStream = new OutputStream(byteArrayOutputStream);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return servletOutputStream;
    }

    private static class OutputStream extends ServletOutputStream {

        private final ByteArrayOutputStream stream;

        public OutputStream(ByteArrayOutputStream stream) {
            this.stream = stream;
        }

        @Override
        public void write(int b) {
            stream.write(b);
        }

        @Override
        public void write(byte[] b) {
            stream.write(b, 0, b.length);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
        }
    }

    /**
     * 数据加密
     */
    public void encryptData(HttpServletResponse response) {
        String sourceString = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        JsonObject jsonObject = JsonParser.parseString(sourceString).getAsJsonObject();

        Pair<String, String> aesPair = RedisCacheManager.getInstance().getRedisAesPair();
        String aesKey = aesPair.getKey();
        String aesIv = aesPair.getValue();
        if (jsonObject.get(CommonConstant.TOTAL_PARAM) != null && !jsonObject.get(CommonConstant.TOTAL_PARAM).isJsonNull()) {
            // 分页响应对象
            PageResult<?> pageResult = JsonUtils.toObject(sourceString, PageResult.class);
            if (pageResult == null) {
                log.error("Server Api Response Wrapper Error: Gson deserializer PageResult is null, source string: {}", sourceString);
                throw new SysException(SysStatus.FAILED);
            }
            String dataJson = JsonUtils.toJson(pageResult.getData());
            String encryptData = AesUtils.encrypt(dataJson, aesKey, aesIv);
            EncryptPageResult result = EncryptPageResult.ok(encryptData, jsonObject.get(CommonConstant.TOTAL_PARAM).getAsInt());
            ServletExtUtils.responseJson(response, result);
            return;
        }
        if (jsonObject.get(CommonConstant.DATA_PARAM) != null && !jsonObject.get(CommonConstant.DATA_PARAM).isJsonNull()) {
            // 结果响应对象
            ApiResult<?> apiResult = JsonUtils.toObject(sourceString, ApiResult.class);
            if (apiResult == null) {
                log.error("Server Api Response Wrapper Error: Gson deserializer ApiResult is null, source string: {}", sourceString);
                throw new SysException(SysStatus.FAILED);
            }
            String dataJson = JsonUtils.toJson(apiResult.getData());
            String encryptData = AesUtils.encrypt(dataJson, aesKey, aesIv);
            EncryptApiResult result = EncryptApiResult.ok(encryptData);
            ServletExtUtils.responseJson(response, result);
            return;
        }

        ServletExtUtils.responseJson(response, sourceString);
    }
}
