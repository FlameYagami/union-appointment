package com.gk.openapi.filter;

import cn.hutool.core.util.StrUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.AesUtils;
import com.gk.common.utils.JsonUtils;
import com.gk.common.utils.ServletExtUtils;
import com.gk.framework.model.bo.system.sysOpenConfig.OpenConfigCache;
import com.gk.openapi.helper.OpenApiHelper;
import com.gk.common.model.others.ApiResult;
import com.gk.server.model.others.EncryptApiResult;
import com.gk.server.model.others.EncryptPageResult;
import com.gk.common.model.others.PageResult;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 响应加密适配器
 *
 * @author Flame
 * @date 2023-03-14 16:44
 **/
@Slf4j
public class OpenApiRespWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream byteArrayOutputStream;
    private final ServletOutputStream servletOutputStream;

    public OpenApiRespWrapper(HttpServletResponse response) {
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
    public void encryptData(HttpServletRequest request, HttpServletResponse response) {
        String sourceString = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        JsonObject jsonObject = JsonParser.parseString(sourceString).getAsJsonObject();

        // 从请求中获取openId
        String openId = request.getHeader(CommonConstant.OPEN_ID_HEADER);
        if (StrUtil.isEmpty(openId)) {
            throw new SysException(SysStatus.NO_AUTHORIZE_ACCESS);
        }

        // 根据openId查询数据库中的第三方配置信息
        OpenConfigCache openConfigCache = OpenApiHelper.getInstance().findOpenConfigCache(openId);

        // 分页返回构建
        if (jsonObject.get(CommonConstant.TOTAL_PARAM) != null && !jsonObject.get(CommonConstant.TOTAL_PARAM).isJsonNull()) {
            // 分页响应对象
            PageResult<?> pageResult = JsonUtils.toObject(sourceString, PageResult.class);
            if (pageResult == null) {
                log.error("OpenApiRespWrapper Error: PageResult is null, source string: {}", sourceString);
                throw new SysException(SysStatus.FAILED);
            }
            String dataJson = JsonUtils.toJson(pageResult.getData());
            String encryptData = AesUtils.encrypt(dataJson, openConfigCache.getAesKey(), openConfigCache.getAesIv());
            EncryptPageResult result = EncryptPageResult.ok(encryptData, jsonObject.get(CommonConstant.TOTAL_PARAM).getAsInt());
            ServletExtUtils.responseJson(response, result);
            return;
        }

        // 普通返回构建
        if (jsonObject.get(CommonConstant.DATA_PARAM) != null && !jsonObject.get(CommonConstant.DATA_PARAM).isJsonNull()) {
            // 结果响应对象
            ApiResult<?> apiResult = JsonUtils.toObject(sourceString, ApiResult.class);
            if (apiResult == null) {
                log.error("OpenApiRespWrapper Error: ApiResult is null, source string: {}", sourceString);
                throw new SysException(SysStatus.FAILED);
            }
            String dataJson = JsonUtils.toJson(apiResult.getData());
            String encryptData = AesUtils.encrypt(dataJson, openConfigCache.getAesKey(), openConfigCache.getAesIv());
            EncryptApiResult result = EncryptApiResult.ok(encryptData);
            ServletExtUtils.responseJson(response, result);
            return;
        }

        ServletExtUtils.responseJson(response, sourceString);

    }
}
