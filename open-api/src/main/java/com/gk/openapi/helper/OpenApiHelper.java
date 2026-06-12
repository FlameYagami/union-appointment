package com.gk.openapi.helper;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.framework.model.bo.system.sysOpenConfig.OpenConfigCache;
import com.gk.framework.service.intf.system.IRedisCacheService;
import lombok.extern.slf4j.Slf4j;

/**
 * 平台帮助类
 *
 * @author Flame
 * @date 2023-03-14 16:44
 **/
@Slf4j
public class OpenApiHelper {

    private final IRedisCacheService redisCacheService;

    public OpenApiHelper() {
        redisCacheService = SpringUtil.getBean(IRedisCacheService.class);
    }

    public static OpenApiHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final OpenApiHelper INSTANCE = new OpenApiHelper();
    }

    public OpenConfigCache findOpenConfigCache(String openId) {
        if (StrUtil.isEmpty(openId)) {
            throw new SysException(SysStatus.NO_AUTHORIZE_ACCESS);
        }
        OpenConfigCache cache = redisCacheService.getOpenConfig(openId);
        if (null == cache) {
            log.error("OpenConfigCache Error: openId({}) can not found", openId);
            throw new SysException(SysStatus.NO_AUTHORIZE_ACCESS);
        }
        return cache;
    }

}
