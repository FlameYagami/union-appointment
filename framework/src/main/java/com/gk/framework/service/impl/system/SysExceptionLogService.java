package com.gk.framework.service.impl.system;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.ExceptionType;
import com.gk.common.utils.JsonUtils;
import com.gk.common.utils.ServletExtUtils;
import com.gk.framework.helper.IpRegionHelper;
import com.gk.framework.mapper.system.SysExceptionLogMapper;
import com.gk.framework.model.dto.system.sysExceptionLog.SysExceptionLogPageReq;
import com.gk.framework.model.dto.system.sysExceptionLog.SysExceptionLogPageResp;
import com.gk.framework.model.entity.system.SysExceptionLog;
import com.gk.framework.service.intf.system.ISysExceptionLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异常日志表 服务实现类
 *
 * @author Flame
 * @since 2024-04-08 10:49:10
 */

@Service
@Slf4j
public class SysExceptionLogService extends ServiceImpl<SysExceptionLogMapper, SysExceptionLog> implements ISysExceptionLogService {

    /**
     * 分页查询
     */
    @Override
    public IPage<SysExceptionLogPageResp> pageList(SysExceptionLogPageReq req) {
        return baseMapper.pageSysExceptionLog(req.createPage(), req);
    }

    /**
     * 保存暴力登录日志
     */
    @Override
    public void saveBruteForceLoginLog(String username, String ip) {
        String location = IpRegionHelper.INSTANCE.getLocation(ip);
        saveAsync(createSysExceptionLog(username, ExceptionType.BRUTE_FORCE_LOGIN, ip, location));
    }

    @Override
    public void saveExceptLog(String username, String ip, ExceptionType exceptionType) {
        String location = IpRegionHelper.INSTANCE.getLocation(ip);
        saveAsync(createSysExceptionLog(username, exceptionType, ip, location));
    }

    /**
     * 保存异地登录日志
     */
    @Override
    public void checkAndSaveRemoteLoginLog(String username, String ip) {
        // ip归属地
        String location = IpRegionHelper.INSTANCE.getLocation(ip);
        // 异地登录判定
        if (IpRegionHelper.INSTANCE.isJiangXiLocation(location) || IpRegionHelper.INSTANCE.isLocation(location)) {
            return;
        }
        // 非江西ip存储日志
        saveAsync(createSysExceptionLog(username, ExceptionType.REMOTE_LOGIN, ip, location));
    }


    private SysExceptionLog createSysExceptionLog(String username, ExceptionType exceptionType, String ip, String location) {
        UserAgent userAgent = UserAgentUtil.parse(ServletExtUtils.getRequest().getHeader("User-Agent"));
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 获取客户端操作系统
        String os = userAgent.getOs().getName();
        // 封装对象
        return new SysExceptionLog()
                .setUsername(username)
                .setExceptionType(exceptionType.value)
                .setIp(ip)
                .setLocation(location)
                .setBrowser(browser)
                .setOs(os);
    }

    @Async
    protected void saveAsync(SysExceptionLog sysExceptionLog) {
        if (!save(sysExceptionLog)) {
            log.error("Save SysExceptionLog Error: {}", JsonUtils.toJson(sysExceptionLog));
        }
    }

}

