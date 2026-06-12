package com.gk.framework.helper;

import cn.hutool.extra.spring.SpringUtil;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.DataStatus;
import com.gk.common.intf.IConfig;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.model.entity.system.SysConfig;
import com.gk.framework.service.intf.system.IRedisCacheService;
import com.gk.framework.service.intf.system.ISysConfigService;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统配置辅助类
 *
 * @author Flame
 * @since 2024-08-26 15:43
 **/
@Slf4j
public class SysConfigHelper {

    private final IRedisCacheService redisCacheService;
    private final ISysConfigService sysConfigService;

    public SysConfigHelper() {
        redisCacheService = SpringUtil.getBean(IRedisCacheService.class);
        sysConfigService = SpringUtil.getBean(ISysConfigService.class);
    }

    /**
     * 懒加载模式
     */
    public static SysConfigHelper getInstance() {
        return SysConfigHelper.Holder.INSTANCE;
    }

    private static class Holder {
        public static final SysConfigHelper INSTANCE = new SysConfigHelper();
    }

    /**
     * 获取 string 类型的值
     */
    public String getString(IConfig config) {
        return getAndSave(config.getName(), config.getKey(), config.getValue());
    }

    /**
     * 获取 int 类型的值
     */
    public int getInt(IConfig config) {
        return Integer.parseInt(getString(config));
    }

    /**
     * 获取 long 类型的值
     */
    public long getLong(IConfig config) {
        return Long.parseLong(getString(config));
    }

    private String getAndSave(String name, String key, String defValue) {
        String value = redisCacheService.getConfigValue(key);
        // 如果没有查询则存储默认值并返回默认值
        if (null == value) {
            log.warn("SysConfig key({}) can not find, save and return default value({})", key, defValue);
            SysConfig sysConfig = new SysConfig()
                    .setConfigName(name)
                    .setConfigKey(key)
                    .setConfigName(key)
                    .setConfigValue(defValue)
                    .setDataStatus(DataStatus.NORMAL.value);
            sysConfig.setCreateBy(CommonConstant.TOP_ID);
            sysConfig.setUpdateBy(CommonConstant.TOP_ID);

            synchronized (sysConfigService) {
                boolean result = sysConfigService.save(sysConfig);
                if (!result) {
                    log.error("SysConfig save error: {}", JsonUtils.toJson(sysConfig));
                }
            }
            return defValue;
        }
        return value;
    }


}
