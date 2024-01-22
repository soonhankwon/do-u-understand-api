package com.douunderstandapi.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.user.enumType.UserStatus;
import java.time.LocalDateTime;
import java.util.function.BiFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("유저 도메인 클래스 단위 테스트")
class UserTest {

    @Test
    @DisplayName("정적 팩터리 메서드 - of")
    void of() {
        String email = "test@gmail.com";
        String password = "password1!";
        User user = createUser(email, password);

        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("유저상태 DELETED 업데이트 - soft delete")
    void soft_delete_user() {
        String email = "test@gmail.com";
        String password = "password1!";
        User user = createUser(email, password);

        user.delete();

        assertThat(user.getUserStatus()).isSameAs(UserStatus.DELETED);
        assertThat(user.getDeletedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("getter 테스트")
    void getter() {
        String email = "test@gmail.com";
        String password = "password1!";
        User user = createUser(email, password);

        assertThat(user.getId()).isNull();
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getIsAuthenticated()).isTrue();
        assertThat(user.getIsAllowedNotification()).isTrue();
    }

    @Test
    void validatePassword() {
        String email = "test@gmail.com";
        String password = "password1!";
        String wrongPassword = "password";
        User user = createUser(email, password);

        BiFunction<String, String, Boolean> mockMatchesFunction = String::equals;

        assertThatThrownBy(() -> user.validatePassword(wrongPassword, mockMatchesFunction))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void validateStatus() {
        String email = "test@gmail.com";
        String password = "password1!";
        User user = createUser(email, password);
        user.delete();

        assertThatThrownBy(user::validateStatus)
                .isInstanceOf(CustomException.class);
    }

    private User createUser(String email, String password) {
        return User.of(email, password, true);
    }
}