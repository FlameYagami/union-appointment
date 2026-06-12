package com.gk.security.service.impl;

import com.gk.security.service.intf.CaptchaCacheService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 验证码Redis缓存实现类
 *
 * @author GuoYu
 * @date 2023-03-01
 */
@Service
public class CaptchaRedisService implements CaptchaCacheService {

    /**
     *  不可使用RedisManager, 因为存储访问次数时会存储成字符串, 调用increment后会保存, 字符串不可自增
     *  所以使用默认的StringRedisTemplate
     */
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void set(String key, String value, long expiresInSeconds) {
        stringRedisTemplate.opsForValue().set(key, value, expiresInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean exists(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    @Override
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

	@Override
	public Long increment(String key, long val) {
        return stringRedisTemplate.opsForValue().increment(key, val);
	}
}
