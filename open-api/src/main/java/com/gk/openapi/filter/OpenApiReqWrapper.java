package com.gk.openapi.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.AesUtils;
import com.gk.framework.model.bo.system.sysOpenConfig.OpenConfigCache;
import com.gk.openapi.helper.OpenApiHelper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * 请求解密适配器
 *
 * @author Flame
 * @date 2023-03-14 16:44
 **/
@Slf4j
public class OpenApiReqWrapper extends HttpServletRequestWrapper {

    private final byte[] rewriteBody; // 用于将流保存下来

    public OpenApiReqWrapper(HttpServletRequest request) {
        super(request);

        String bodyString = ServletUtil.getBody(request);
        if (StrUtil.isEmpty(bodyString)) {
            log.error("OpenApiReqWrapper Error: body is empty");
            throw new SysException(SysStatus.ILLEGAL_PARAM);
        }

        // 从请求中获取openId
        String openId = request.getHeader(CommonConstant.OPEN_ID_HEADER);
        if (StrUtil.isEmpty(openId)) {
            throw new SysException(SysStatus.NO_AUTHORIZE_ACCESS);
        }

        // 根据openId查询数据库中的第三方配置信息
        OpenConfigCache openConfigCache = OpenApiHelper.getInstance().findOpenConfigCache(openId);

        // 签名验证
        String sign = request.getHeader(CommonConstant.SIGN_HEADER);
        if (sign.isEmpty()) {
            log.error("OpenApiReqWrapper Error: sign is empty");
            throw new SysException(SysStatus.DATA_SIGN_ERROR);
        }
        String signMd5 = DigestUtil.md5Hex(openId + bodyString + openConfigCache.getAesKey());
        if (!signMd5.equals(sign)) {
            log.error("OpenApiReqWrapper Error, illegal sign: openId({}), data({}), sign({})", openId, bodyString, sign);
            throw new SysException(SysStatus.DATA_SIGN_ERROR);
        }

        // 数据解密
        String data = AesUtils.decrypt(bodyString, openConfigCache.getAesKey(), openConfigCache.getAesIv());

        // 数据判空
        if (StrUtil.isEmpty(data)) {
            throw new SysException(SysStatus.DATA_FORMAT_ERROR);
        }

        // 数据拆分(Json + 时间戳)
        String[] dataItems = data.split(CommonConstant.DATA_SEPARATOR_MULTI);
        if (2 != dataItems.length) {
            log.error("OpenApiReqWrapper Error, illegal data: {}", data);
            throw new SysException(SysStatus.DATA_FORMAT_ERROR);
        }

        // todo 对时间戳进行校验

        String dataJson = dataItems[0];
        log.info("OpenApiReqWrapper data: {}", dataJson);

        rewriteBody = dataJson.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public ServletInputStream getInputStream() {
        // 返回的是我们处理之后的数据
        ByteArrayInputStream stream = new ByteArrayInputStream(rewriteBody);
        return new ServletInputStream() {

            @Override
            public int read() {
                return stream.read();  // 读取 requestBody 中的数据
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

}
