package com.gk.framework.manager;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作封装类
 *
 * @author GuoYu
 * @since 2022-10-09 14:57
 **/
@Component
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Slf4j
public class RedisManager {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 批量检索
     *
     * @param pattern 检索规则
     */
    public List<String> matchScan(String pattern) {
        return (List<String>) redisTemplate.execute((RedisCallback<List<String>>) connection -> {
            List<String> keyList = new ArrayList<>();
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(pattern).count(1000).build());

            while (cursor.hasNext()) {
                keyList.add(new String(cursor.next()));
            }
            return keyList;
        });
    }

    /**
     * 给一个指定的 key 值附加过期时间(秒)
     *
     * @param key  键
     * @param time 过期时间(秒)
     */
    public boolean expire(String key, long time) {
        if (!hasKey(key)) {
            log.error("Expire Redis Key Error: Key({}) has not found", key);
            return false;
        }
        return Boolean.TRUE.equals(redisTemplate.expire(key, time, TimeUnit.SECONDS));
    }

    /**
     * 给一个指定的 key 值附加过期时间
     *
     * @param key  键
     * @param time 过期时间
     * @param timeUnit 时间单位
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        if (!hasKey(key)) {
            log.error("Expire Redis Key Error: Key({}) has not found", key);
            return false;
        }
        return Boolean.TRUE.equals(redisTemplate.expire(key, time, timeUnit));
    }

    /**
     * 根据 key 获取过期时间
     *
     * @param key 键
     */
    public Long getTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断 key 是否有对应的值
     *
     * @param key 键
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 把 key-value 转为永久存在
     *
     * @param key 键
     */
    public boolean persist(String key) {
        return Boolean.TRUE.equals(redisTemplate.boundValueOps(key).persist());
    }

    //- - - - - - - - - - - - - - - - - - - - -  Object类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 根据 key 获取值
     *
     * @param key 键
     */
    public <T> T get(String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除指定 Key
     *
     * @param key 键
     */
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 批量删除指定的 Key集合
     *
     * @param keyList 键集合
     */
    public void deleteBatch(List<String> keyList) {
        if (CollUtil.isEmpty(keyList)) {
            return;
        }

        redisTemplate.delete(keyList);
    }

    /**
     * 将值放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 将值放入缓存并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) -1为无期限
     */
    public void set(String key, Object value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 将值放入缓存并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) -1为无期限
     */
    public void set(String key, Object value, long time, TimeUnit timeUnit) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, timeUnit);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 批量添加 key (重复的键会覆盖)
     *
     * @param keyValueMap map键值对
     */
    public void batchSet(Map<String, Object> keyValueMap) {
        redisTemplate.opsForValue().multiSet(keyValueMap);
    }

    /**
     * 批量添加 key-value 只有在键不存在时,才添加
     * map 中只要有一个key存在,则全部不添加
     *
     * @param keyValueMap map键值对
     */
    public void batchSetIfAbsent(Map<String, Object> keyValueMap) {
        redisTemplate.opsForValue().multiSetIfAbsent(keyValueMap);
    }

    /**
     * 对一个 key-value 的值进行递增操作,
     *
     * @param key 键
     */
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 对一个 key-value 的值进行递增操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是长整型 ,将报错
     *
     * @param key    键
     * @param number 步进长度
     */
    public Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 对一个 key-value 的值进行递增操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是 纯数字 ,将报错
     *
     * @param key    键
     * @param number 步进长度
     */
    public Double increment(String key, double number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 对一个 key-value 的值进行递减操作,
     *
     * @param key 键
     */
    public Long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    /**
     * 对一个 key-value 的值进行递减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是长整型 ,将报错
     *
     * @param key    键
     * @param number 步进长度
     */
    public Long decrement(String key, long number) {
        return redisTemplate.opsForValue().decrement(key, number);
    }

    //- - - - - - - - - - - - - - - - - - - - -  set类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 将数据放入 set 缓存
     *
     * @param key 键
     */
    public void sSet(String key, Object... value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 获取变量中的值
     *
     * @param key 键
     */
    public <T> Set<T> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 随机获取变量中的一个元素
     *
     * @param key 键
     */
    public <T> T sRandomMember(String key) {
        SetOperations<String, T> operation = redisTemplate.opsForSet();
        return operation.randomMember(key);
    }

    /**
     * 随机获取变量中指定个数的元素
     *
     * @param key   键
     * @param count 指定取出的个数
     */
    public void sRandomMembers(String key, long count) {
        redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 弹出变量中的元素
     *
     * @param key 键
     */
    public <T> T sPop(String key) {
        SetOperations<String, T> operation = redisTemplate.opsForSet();
        return operation.pop(key);
    }

    /**
     * 获取变量中值的长度
     *
     * @param key 键
     */
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     */
    public boolean sHasKey(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    /**
     * 检查给定的元素是否在变量中。
     *
     * @param key 键
     * @param obj 元素对象
     */
    public boolean isSetMember(String key, Object obj) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, obj));
    }

    /**
     * 转移变量的元素值到目的变量。
     *
     * @param key     键
     * @param value   元素对象
     * @param destKey 元素对象
     */
    public boolean sMove(String key, Object value, String destKey) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().move(key, value, destKey));
    }

    /**
     * 批量移除set缓存中元素
     *
     * @param key    键
     * @param values 值
     */
    public void sRemove(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 通过给定的key求2个set变量的差值
     *
     * @param key     键
     * @param destKey 键
     */
    public <T> Set<T> sDifference(String key, String destKey) {
        SetOperations<String, T> operation = redisTemplate.opsForSet();
        return operation.difference(key, destKey);
    }


    //- - - - - - - - - - - - - - - - - - - - -  hash类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 存入hash缓存
     *
     * @param key     键
     * @param hashKey hash键
     * @param value   值
     */
    public void hashPut(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 存入hash缓存
     *
     * @param key 键
     * @param map Map
     */
    public void hashPutAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 存入hash缓存
     *
     * @param key  键
     * @param map  Map
     * @param time 时间(秒) -1为无期限
     */
    public void hashPutAll(String key, Map<String, Object> map, long time, TimeUnit timeUnit) {
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 获取 key 下的 所有 hashKey 和 value
     *
     * @param key 键
     */
    public <T> Map<String, T> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 验证指定 key 下 有没有指定的 hashKey
     *
     * @param key     键
     * @param hashKey hash键
     */
    public boolean hasHashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 获取指定key的值
     *
     * @param key     键
     * @param hashKey hash键
     */
    public <T> T getHashValue(String key, String hashKey) {
        HashOperations<String, String, T> operations = redisTemplate.opsForHash();
        return operations.get(key, hashKey);
    }

    /**
     * 获取指定key的值
     *
     * @param key     键
     * @param hashKey hash键
     */
    public Long getLongHashValue(String key, String hashKey) {
        HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
        Object value = operations.get(key, hashKey);
        if (null == value) {
            return null;
        }
        return Long.parseLong(value.toString());
    }

    /**
     * 删除指定 hash 的 hashKey
     *
     * @param key      键
     * @param hashKeys hash键
     */
    public Long hashDelete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 给指定 hash 的 hashKey 做增减操作
     *
     * @param key     键
     * @param hashKey hash键
     * @param number  步进长度
     */
    public Long hashIncrement(String key, String hashKey, long number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 给指定 hash 的 hashKey 做增减操作
     *
     * @param key     键
     * @param hashKey hash键
     * @param number  步进长度
     */
    public Double hashIncrement(String key, String hashKey, Double number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 获取 key 下的 所有 hashKey 字段
     *
     * @param key 键
     */
    public <T> Set<T> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取指定 hash 下面的 键值对 数量
     *
     * @param key 键
     */
    public Long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    //- - - - - - - - - - - - - - - - - - - - -  list类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 在变量左边添加元素值
     *
     * @param key   键
     * @param value 值
     */
    public void listLeftPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 获取集合指定位置的值。
     *
     * @param key   键
     * @param index 序号
     */
    public <T> T listIndex(String key, long index) {
        ListOperations<String, T> operations = redisTemplate.opsForList();
        return operations.index(key, index);
    }

    /**
     * 获取指定区间的值。
     *
     * @param key   键
     * @param start 区间起始序号
     * @param end   区间结束序号
     */
    public <T> List<T> listRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 把最后一个参数值放到指定集合的第一个出现中间参数的前面，
     * 如果中间参数值存在的话。
     *
     * @param key   键
     * @param pivot 指定的值
     * @param value 值
     */
    public void listLeftPush(String key, Object pivot, Object value) {
        redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     * 向左边批量添加参数元素。
     *
     * @param key    键
     * @param values 值
     */
    public void listLeftPushAll(String key, Object... values) {
        redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 向集合最右边添加元素。
     *
     * @param key   键
     * @param value 值
     */
    public void listLeftPushAll(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 向左边批量添加参数元素。
     *
     * @param key    键
     * @param values 值
     */
    public void listRightPushAll(String key, Object... values) {
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 向已存在的集合中添加元素。
     *
     * @param key   键
     * @param value 值
     */
    public void listRightPushIfPresent(String key, Object value) {
        redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 向已存在的集合中添加元素。
     *
     * @param key 键
     */
    public Long listLength(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 移除集合中的左边第一个元素。
     *
     * @param key 键
     */
    public <T> T listLeftPop(String key) {
        ListOperations<String, T> operations = redisTemplate.opsForList();
        return operations.leftPop(key);
    }

    /**
     * 移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
     *
     * @param key     键
     * @param timeout 等待时长
     * @param unit    时间单位
     */
    public <T> T listLeftPop(String key, long timeout, TimeUnit unit) {
        ListOperations<String, T> operations = redisTemplate.opsForList();
        return operations.leftPop(key, timeout, unit);
    }

    /**
     * 移除集合中右边的元素。
     *
     * @param key 键
     */
    public <T> T listRightPop(String key) {
        ListOperations<String, T> operations = redisTemplate.opsForList();
        return operations.rightPop(key);
    }

    /**
     * 移除集合中右边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
     *
     * @param key 键
     */
    public <T> T listRightPop(String key, long timeout, TimeUnit unit) {
        ListOperations<String, T> operations = redisTemplate.opsForList();
        return operations.rightPop(key, timeout, unit);
    }

}
