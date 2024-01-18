package com.douunderstandapi.auth.repository.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthEmailCodeRepository {
    private static final String AUTH_EMAIL_CODE_PREFIX = "auth_email_code:";
    private static final long AUTH_EMAIL_CODE_TTL = 1L;

    private final RedisTemplate<String, String> redisTemplate;

    public void put(String key, String value) {
        String redisKey = AUTH_EMAIL_CODE_PREFIX + key;
        this.redisTemplate.opsForValue().set(
                redisKey,
                value,
                AUTH_EMAIL_CODE_TTL,
                TimeUnit.HOURS);
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(this.redisTemplate.opsForValue().get(AUTH_EMAIL_CODE_PREFIX + key));
    }

    public void delete(String key) {
        this.redisTemplate.delete(AUTH_EMAIL_CODE_PREFIX + key);
    }
}
