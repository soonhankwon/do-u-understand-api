package com.douunderstandapi.user.repository.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserEmailAuthCodeRepositoryTest {

    @Autowired
    UserEmailAuthCodeRepository userEmailAuthCodeRepository;
    
    @AfterEach
    void cleanResource() {
        String key = "test@gmail.com";
        userEmailAuthCodeRepository.delete(key);
    }

    @Test
    void putAndGet() {
        String key = "test@gmail.com";
        String value = UUID.randomUUID().toString();

        userEmailAuthCodeRepository.put(key, value);

        String res = null;
        Optional<String> optionalValue = userEmailAuthCodeRepository.get(key);
        if (optionalValue.isPresent()) {
            res = optionalValue.get();
        }

        assertThat(res).isEqualTo(value);
    }

    @Test
    void delete() {
        String key = "test@gmail.com";
        String value = UUID.randomUUID().toString();

        userEmailAuthCodeRepository.put(key, value);

        userEmailAuthCodeRepository.delete(key);
        Optional<String> optionalValue = userEmailAuthCodeRepository.get(key);

        assertThat(optionalValue).isEmpty();
    }
}