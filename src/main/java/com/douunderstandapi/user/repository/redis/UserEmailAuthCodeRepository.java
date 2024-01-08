package com.douunderstandapi.user.repository.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserEmailAuthCodeRepository {
    private static final String USER_EMAIL_AUTH_CODE_PREFIX = "user_email_auth_code:";
    private static final long EMAIL_AUTH_CODE_TTL = 1L;

    private final RedisTemplate<String, String> redisTemplate;

    public void put(String key, String value) {
        String redisKey = USER_EMAIL_AUTH_CODE_PREFIX + key;
        this.redisTemplate.opsForValue().set(
                redisKey,
                value,
                EMAIL_AUTH_CODE_TTL,
                TimeUnit.HOURS);
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(this.redisTemplate.opsForValue().get(USER_EMAIL_AUTH_CODE_PREFIX + key));
    }

    public void delete(String key) {
        this.redisTemplate.delete(USER_EMAIL_AUTH_CODE_PREFIX + key);
    }
}
