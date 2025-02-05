package com.clone.twitter.url_shortener_service.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UrlCacheRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Optional<String> getUrlByHash(String hash) {
        return Optional.ofNullable(redisTemplate.opsForValue()
                .get(hash));
    }

    public void saveUrlByHash(String url, String hash) {
        redisTemplate.opsForValue().set(hash, url);
    }
}
