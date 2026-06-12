package com.gk.security.aspect;

import com.gk.common.enums.OperateStatus;
import com.gk.common.utils.AspectLogUtils;
import com.gk.common.utils.IpUtils;
import com.gk.common.utils.JsonUtils;
import com.gk.common.utils.ServletExtUtils;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.model.entity.system.SysOperateLog;
import com.gk.framework.service.intf.system.ISysOperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 操作日志记录处理
 *
 * @author Flame
 * @since 2022-11-20 10:16
 */

@Aspect
@Component
@Slf4j
public class OperateLogAspect {

    @Resource
    private ISysOperateLogService iSysOperateLogService;

    /**
     * 处理完请求后执行
     */
    @AfterReturning(pointcut = "@annotation(operateLog)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, OperateLog operateLog, Object result) {
        handleLog(joinPoint, operateLog, result, null);
    }

    /**
     * 拦截异常操作
     */
    @AfterThrowing(value = "@annotation(operateLog)", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, OperateLog operateLog, Exception exception) {
        handleLog(joinPoint, operateLog, null, exception);
    }

    protected void handleLog(final JoinPoint joinPoint, OperateLog operateLog, Object resultJson, Exception resultException) {
        // 不记日志直接返回
        if (!operateLog.enable()) {
            return;
        }

        HttpServletRequest request = ServletExtUtils.getRequest();
        String requestMethod = request.getMethod();

        try {
            long deptId = RedisCacheManager.getInstance().getRedisDeptId();
            SysOperateLog sysOperateLog = new SysOperateLog()
                    .setDeptId(deptId)
                    .setTitle(operateLog.title()) // 设置标题
                    .setRequestUrl(request.getRequestURI()) // 设置操作url
                    .setRequestMethod(request.getMethod()) // 设置请求方式
                    .setType(operateLog.operateType().value) // 设置业务类型
                    .setIp(IpUtils.getIpAddress(request))
                    .setStatus(null == resultException ? OperateStatus.SUCCESS.value : OperateStatus.FAILED.value);

            // 是否需要保存request
            if (operateLog.logParam()) {
                String params;
                if (HttpMethod.POST.name().equals(requestMethod) || HttpMethod.PUT.name().equals(requestMethod)
                        || HttpMethod.PATCH.name().equals(requestMethod) || HttpMethod.DELETE.name().equals(requestMethod)) {
                    params = AspectLogUtils.obtainMethodParam(joinPoint);
                } else {
                    Map<?, ?> paramsMap = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                    params = paramsMap.toString();
                }
                sysOperateLog.setParam(params);
            }
            // 是否需要保存response(保存结果或是异常)
            if (operateLog.logResult() && (null != resultJson || null != resultException)) {
                String result = JsonUtils.toJson(null != resultJson ? resultJson : resultException.getMessage());
                sysOperateLog.setResult(result);
            }

            // 保存操作日志
            iSysOperateLogService.save(sysOperateLog);
        } catch (Exception exception) {
            log.error("Save SysOperateLog error:{}", exception.getMessage());
        }
    }

}
