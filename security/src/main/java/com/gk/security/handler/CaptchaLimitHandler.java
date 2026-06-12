package com.gk.security.handler;

import cn.hutool.core.util.StrUtil;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.security.constant.CaptchaConstant;
import com.gk.security.model.bo.CaptchaParam;
import com.gk.security.service.intf.CaptchaCacheService;
import com.gk.security.utils.CaptchaImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 行为验证码请求限制器
 *
 * @author Kevin
 * @since 2023-08-09 15:30
 **/
@Component
@Slf4j
public class CaptchaLimitHandler {

    @Resource
    private CaptchaParam captchaParam;

    @Resource
    private CaptchaCacheService captchaRedisService;

    private static final String GET_TYPE = "GET";
    private static final String LOCK_TYPE = "LOCK";
    private static final String CHECK_TYPE = "CHECK";
    private static final String FAIL_TYPE = "FAIL";

    private static final String DEFAULT_LIMIT_COUNT = "1";

    @PostConstruct
    private void init() {
        initImageMap(captchaParam.getBlockPuzzlePath(), captchaParam.getClickWordPath());
    }

    private String captchaLimitKey(String clientUid, String type) {
        return String.format(CaptchaConstant.CAPTCHA_LIMIT_KEY, type, clientUid);
    }

    public void validateGet(String clientUid) {
        // 无客户端身份标识，不限制
        if (StrUtil.isEmpty(clientUid)) {
            return;
        }
        String getKey = captchaLimitKey(clientUid, GET_TYPE);
        String lockKey = captchaLimitKey(clientUid, LOCK_TYPE);
        // 失败次数过多，锁定
        if (Objects.nonNull(captchaRedisService.get(lockKey))) {
            throw new SysException(SysStatus.CAPTCHA_REQ_GET_LOCKED);
        }
        String getCount = captchaRedisService.get(getKey);
        if (Objects.isNull(getCount)) {
            getCount = DEFAULT_LIMIT_COUNT;
            captchaRedisService.set(getKey, getCount, CaptchaConstant.EXPIRE_LIMIT_DURATION);
        }
        captchaRedisService.increment(getKey, 1);
        // 1分钟内请求次数过多
        if (Long.parseLong(getCount) > captchaParam.getReqGetMinuteLimit()) {
            throw new SysException(SysStatus.CAPTCHA_REQ_GET_LIMITED);
        }

        // 失败次数验证
        String failKey = captchaLimitKey(clientUid, FAIL_TYPE);
        String failCount = captchaRedisService.get(failKey);
        // 没有验证失败，通过校验
        if (Objects.isNull(failCount)) {
            return;
        }
        // 1分钟内失败次数过多, 则锁定get接口
        if (Long.parseLong(failCount) > captchaParam.getReqGetLockLimit()) {
            captchaRedisService.set(lockKey, DEFAULT_LIMIT_COUNT, captchaParam.getReqGetLockDuration());
            throw new SysException(SysStatus.CAPTCHA_REQ_GET_LOCKED);
        }
    }

    public void validateCheck(String clientUid) {
        // 无客户端身份标识，不限制
        if (StrUtil.isEmpty(clientUid)) {
            return;
        }
        String checkLimitKey = captchaLimitKey(clientUid, CHECK_TYPE);
        String limitCount = captchaRedisService.get(checkLimitKey);
        if (Objects.isNull(limitCount)) {
            limitCount = DEFAULT_LIMIT_COUNT;
            captchaRedisService.set(checkLimitKey, limitCount, CaptchaConstant.EXPIRE_LIMIT_DURATION);
        }
        captchaRedisService.increment(checkLimitKey, 1);
        if (Long.parseLong(limitCount) > captchaParam.getReqCheckMinuteLimit()) {
            throw new SysException(SysStatus.CAPTCHA_REQ_CHECK_LIMITED);
        }
    }

    public void afterValidateFail(String clientUid) {
        if (captchaParam.isReqLimit()) {
            // 验证失败 分钟内计数
            String failLimitKey = String.format(CaptchaConstant.CAPTCHA_LIMIT_KEY, FAIL_TYPE, clientUid);
            if (!captchaRedisService.exists(failLimitKey)) {
                captchaRedisService.set(failLimitKey, DEFAULT_LIMIT_COUNT, CaptchaConstant.EXPIRE_LIMIT_DURATION);
            }
            captchaRedisService.increment(failLimitKey, 1);
        }
    }

    private void initImageMap(String blockPuzzlePath, String clickWordPath) {
        CaptchaImageUtils.cacheCaptchaImage(getResourcesImagesFile(blockPuzzlePath + "/bgImage/*.png"),
                getResourcesImagesFile(blockPuzzlePath + "/slideBlock/*.png"),
                getResourcesImagesFile(clickWordPath + "/*.png"));
    }

    private Map<String, String> getResourcesImagesFile(String path) {
        Map<String, String> imgMap = new HashMap<>();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            org.springframework.core.io.Resource[] resources = resolver.getResources(path);
            for (org.springframework.core.io.Resource resource : resources) {
                byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
                String string = Base64Utils.encodeToString(bytes);
                String filename = resource.getFilename();
                imgMap.put(filename, string);
            }
        } catch (Exception e) {
            log.error("Get resource image file error: ", e);
        }
        return imgMap;
    }
}
