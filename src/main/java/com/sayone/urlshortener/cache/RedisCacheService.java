package com.sayone.urlshortener.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final RedisTemplate<String, String> redis;

    public void put(String shortCode, String originalUrl, ZonedDateTime expiry){
        try{
            long ttlSeconds = ChronoUnit.SECONDS.between(ZonedDateTime.now(), expiry);
            redis.opsForValue().set(shortCode, originalUrl, ttlSeconds);
        } catch(Exception e){
            System.out.println("Redis Cache Error" + e.getMessage());
        }
    }

    public String get(String shortCode){
        try{
            return redis.opsForValue().get(shortCode);
        }  catch(Exception e){
            System.out.println("Redis Cache Error" + e.getMessage());
        }
    }

    public void evict(String shortCode){
        try{
            redis.delete(shortCode);
        } catch(Exception e){
            System.out.println("Redis Cache Error" + e.getMessage());
        }
    }


}
