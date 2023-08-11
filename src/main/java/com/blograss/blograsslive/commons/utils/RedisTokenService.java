package com.blograss.blograsslive.commons.utils;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTokenService {

    private final StringRedisTemplate redisTemplate;

    public RedisTokenService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveGithubToken(String userId, String accessToken, String refreshToken, Integer accessExpiresIn, Integer refreshExpiresIn) {
        redisTemplate.opsForValue().set(accessToken, userId, Duration.ofSeconds(accessExpiresIn));
        redisTemplate.opsForValue().set(refreshToken, userId, Duration.ofSeconds(refreshExpiresIn));
    }

    public String getAccessToken(String accessToken) {
        return redisTemplate.opsForValue().get(accessToken);
    }

    public String getRefreshToken(String refreshToken) {
        return redisTemplate.opsForValue().get(refreshToken);
    }

    public void removeTokens(String accessToken, String refreshToken) {
        redisTemplate.delete(accessToken);
        redisTemplate.delete(refreshToken);
    }

     public void removeRefreshTokens(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }
}