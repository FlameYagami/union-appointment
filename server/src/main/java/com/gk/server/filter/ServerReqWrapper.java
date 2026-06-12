package com.gk.server.filter;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.AesUtils;
import com.gk.framework.filter.AbstractServletRequestWrapper;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * 接口请求解密封装类
 *
 * @author GuoYu
 * @since 2023-03-09 13:34
 */

@Slf4j
public class ServerReqWrapper extends AbstractServletRequestWrapper {

    /**
     * 解密后的请求参数
     */
    private final byte[] body;

    public ServerReqWrapper(HttpServletRequest request) {
        super(request);

        String requestBody = ServletUtil.getBody(request);
        if (StrUtil.isEmpty(requestBody)) {
            log.error("Server Api Request Wrapper Error: request body data is blank");
            throw new SysException(SysStatus.ILLEGAL_PARAM);
        }

        String token = TokenUtils.getRequestToken(request);
        Pair<String, String> aesPair = RedisCacheManager.getInstance().getRedisAesPair();
        String aesKey = aesPair.getKey();
        String aesIv = aesPair.getValue();
        if (StrUtil.isEmpty(aesKey) || StrUtil.isEmpty(aesIv)) {
            log.error("Server Api Request Wrapper Error: AES Key Or AES IV not found");
            throw new SysException(SysStatus.AES_KEY_IV_ERROR);
        }

        // 签名验证
        String reqSign = request.getHeader(CommonConstant.SIGN_HEADER);
        String sign = DigestUtil.md5Hex(token + requestBody + aesKey);

        if (!sign.equals(reqSign)) {
            log.error("Server Api Request Wrapper Error: Illegal sign, sign({}), request sign({})", sign, reqSign);
            throw new SysException(SysStatus.DATA_SIGN_ERROR);
        }

        // 解密完的数据直接塞回请求
        body = AesUtils.decryptBytes(requestBody, aesKey, aesIv);
    }

    @Override
    protected byte[] getBody() {
        return body;
    }

}
