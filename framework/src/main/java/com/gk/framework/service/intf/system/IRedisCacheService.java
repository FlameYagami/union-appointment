package com.gk.framework.service.intf.system;

import com.gk.framework.model.bo.system.sysOpenConfig.OpenConfigCache;
import com.gk.framework.model.dto.system.CachedDictData;
import com.gk.framework.model.dto.system.CachedUserData;

import java.util.List;

/**
 * Redis缓存 服务接口类
 *
 * @author Flame
 * @date 2023-04-28 15:33
 **/
public interface IRedisCacheService {

    CachedUserData getUserData(long userId);

    List<CachedDictData> getDictData(String dictCode);

    String getConfigValue(String configKey);

    OpenConfigCache getOpenConfig(String openId);


}
