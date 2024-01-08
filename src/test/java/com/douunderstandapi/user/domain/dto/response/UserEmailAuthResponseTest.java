package com.douunderstandapi.user.domain.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserEmailAuthResponseTest {

    @Test
    void from() {
        String code = UUID.randomUUID().toString();
        UserEmailAuthResponse response = UserEmailAuthResponse.from(code);

        assertThat(response.code()).isEqualTo(code);
    }
}