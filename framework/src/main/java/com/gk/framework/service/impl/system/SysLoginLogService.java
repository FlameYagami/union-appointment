package com.gk.framework.service.impl.system;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.OperateStatus;
import com.gk.common.utils.IpUtils;
import com.gk.common.utils.JsonUtils;
import com.gk.common.utils.ServletExtUtils;
import com.gk.framework.annotation.DataScope;
import com.gk.framework.mapper.system.SysLoginLogMapper;
import com.gk.framework.model.dto.system.sysLoginLog.SysLoginLogPageReq;
import com.gk.framework.model.dto.system.sysLoginLog.SysLoginLogPageResp;
import com.gk.framework.model.entity.system.SysLoginLog;
import com.gk.framework.service.intf.system.ISysLoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author Flame
 * @since 2022-11-20 10:16
 */
@Service
@Slf4j
public class SysLoginLogService extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {

    /**
     * 分页查询
     */
    @DataScope(bizTableAlias = "sll")
    @Override
    public IPage<SysLoginLogPageResp> pageList(SysLoginLogPageReq req) {
        return baseMapper.pageSysLoginLog(req.createPage(), req);
    }

    /**
     * 保存系统登录信息
     */
    @Override
    public void saveSysLoginInfo(String username, Long deptId, String resultMsg, OperateStatus operateStatus) {
        UserAgent userAgent = UserAgentUtil.parse(ServletExtUtils.getRequest().getHeader("User-Agent"));
        String loginIp = IpUtils.getIpAddress(ServletExtUtils.getRequest());
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 获取客户端操作系统
        String os = userAgent.getOs().getName();
        // 封装对象
        SysLoginLog sysLoginLog = new SysLoginLog()
                .setUsername(username)
                .setDeptId(deptId)
                .setLoginIp(loginIp)
                .setBrowser(browser)
                .setOs(os)
                .setResultMsg(resultMsg)
                .setStatus(operateStatus.value);

        saveAsync(sysLoginLog);
    }

    @Async
    protected void saveAsync(SysLoginLog sysLoginLog) {
        if (!save(sysLoginLog)) {
            log.error("Save SysLoginLog Error: {}", JsonUtils.toJson(sysLoginLog));
        }
    }


}
