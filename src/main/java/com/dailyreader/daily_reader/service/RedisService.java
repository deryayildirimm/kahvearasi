package com.dailyreader.daily_reader.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;


    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isContentAlreadySent(Long userId, LocalDate date) {
        String key = generateKey(userId, date);
        return redisTemplate.hasKey(key);
    }

    public void markContentAsSent(Long userId, LocalDate date, Long contentId) {
        String key = generateKey(userId, date);
        redisTemplate.opsForValue().set(key, contentId.toString(), Duration.ofDays(1));
    }

    private String generateKey(Long userId, LocalDate date) {
        return "sent:" + userId + ":" + date.toString();
    }
}
