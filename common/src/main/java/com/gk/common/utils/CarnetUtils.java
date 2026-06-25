package com.gk.common.utils;

import cn.hutool.core.util.StrUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 通关卡工具类
 *
 * @author GuoYu
 * @since 2025-03-03 11:04
 **/
@Slf4j
public class CarnetUtils {
    /**
     * 通关解密后, 可以拆分成 AES KEY 和 AES IV 两部分, 所以长度是2
     */
    private static final int CARNET_SIZE = 2;

    private CarnetUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 解密通关卡
     *
     * @return AES Key/IV组成的字符串数组
     */
    public static List<String> decryptCarnet(String carnet) {
        if (StrUtil.isEmpty(carnet)) {
            log.error("Login Error: carnet is blank");
            throw new SysException(SysStatus.LOGIN_FAILED);
        }
        String privateKey = RsaUtils.getPrivateKey();
        log.debug("Private Key: {}", privateKey);
        String decryptCarnetStr = RsaUtils.decryptBase64(carnet, privateKey);
        log.debug("Decrypt Carnet: {}", decryptCarnetStr);

        List<String> decryptCarnetList = StrUtil.split(decryptCarnetStr, CommonConstant.PIPE);
        if (decryptCarnetList.size() != CARNET_SIZE) {
            log.error("Login Error: carnet split size is not 2");
            throw new SysException(SysStatus.LOGIN_FAILED);
        }
        return decryptCarnetList;
    }
}
