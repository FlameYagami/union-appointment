package com.gk.security.service.intf;

/**
 * 行为验证码 缓存接口层
 *
 * @author Kevin
 * @date 2023-08-11 15:06
 */
public interface CaptchaCacheService {

	void set(String key, String value, long expiresInSeconds);

	boolean exists(String key);

	void delete(String key);

	String get(String key);

	/***
	 *
	 * @param key
	 * @param val
	 * @return
	 */
	default Long increment(String key, long val){
		return 0L;
	};

}
