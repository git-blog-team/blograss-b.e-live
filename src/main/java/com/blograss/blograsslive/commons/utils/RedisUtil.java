package com.blograss.blograsslive.commons.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Object> redisBlackListTemplate;

    RedisUtil(
            RedisTemplate<String, Object> redisTemplate,
            RedisTemplate<String, Object> redisBlackListTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisBlackListTemplate = redisBlackListTemplate;
    }

    public void set(String key, Object o, int minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(o.getClass()));
        redisTemplate.opsForValue().set(key, o, minutes, TimeUnit.MINUTES);
    }

    public Object get(String key) {
        Object o = redisTemplate.opsForValue().get(key);
        if (o instanceof String)
            o = ((String) o).replaceAll("\"", "");
        return o;
    }

    public boolean delete(String key) {

        return redisTemplate.delete(key);

    }

    // public boolean hasKey(String key) {
    // return redisTemplate.hasKey(key);
    // }
    //
    // public void setBlackList(String key, Object o, int minutes) {
    // redisBlackListTemplate.setValueSerializer(new
    // Jackson2JsonRedisSerializer(o.getClass()));
    // redisBlackListTemplate.opsForValue().set(key, o, minutes, TimeUnit.MINUTES);
    // }

    // public Object getBlackList(String key) {
    // return redisBlackListTemplate.opsForValue().get(key);
    // }
    //
    // public boolean deleteBlackList(String key) {
    // return redisBlackListTemplate.delete(key);
    // }
    //
    // public boolean hasKeyBlackList(String key) {
    // return redisBlackListTemplate.hasKey(key);
    // }
}