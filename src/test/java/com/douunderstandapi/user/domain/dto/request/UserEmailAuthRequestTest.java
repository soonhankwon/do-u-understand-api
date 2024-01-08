package com.douunderstandapi.user.domain.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserEmailAuthRequestTest {

    @Test
    void email() {
        String email = "test@gmail.com";
        UserEmailAuthRequest request = new UserEmailAuthRequest(email);

        assertThat(request.email()).isEqualTo(email);
    }
}