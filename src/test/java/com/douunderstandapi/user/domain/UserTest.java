package com.douunderstandapi.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("유저 도메인 클래스 단위 테스트")
class UserTest {

    @Test
    @DisplayName("정적 팩터리 메서드 - of")
    void of() {
        String email = "test@gmail.com";
        String password = "password1!";
        User user = User.of(email, password);

        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("getter 테스트")
    void getter() {
        String email = "test@gmail.com";
        String password = "password1!";
        User user = User.of(email, password);

        assertThat(user.getId()).isNull();
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getIsAuthenticated()).isFalse();
    }
}