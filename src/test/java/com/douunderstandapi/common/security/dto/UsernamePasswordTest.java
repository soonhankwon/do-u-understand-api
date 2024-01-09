package com.douunderstandapi.common.security.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UsernamePasswordTest {

    @Test
    void getter_record() {
        String username = "test@gmail.com";
        String password = "password1!";
        UsernamePassword usernamePassword = new UsernamePassword(username, password);

        Assertions.assertThat(usernamePassword.username()).isEqualTo(username);
        Assertions.assertThat(usernamePassword.password()).isEqualTo(password);
    }
}