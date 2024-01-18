package com.douunderstandapi.user.domain.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.dto.response.UserDeleteResponse;
import com.douunderstandapi.user.enumType.UserStatus;
import org.junit.jupiter.api.Test;

class UserDeleteResponseTest {

    @Test
    void of() {
        Long id = 1L;
        String email = "test@gmail.com";
        UserStatus status = UserStatus.DELETED;

        UserDeleteResponse response = UserDeleteResponse.of(id, email, status);

        assertThat(response.id()).isEqualTo(id);
    }

    @Test
    void from() {
        String email = "test@gmail.com";
        User user = createUser(email);
        user.delete();

        UserDeleteResponse response = UserDeleteResponse.from(user);

        assertThat(response.id()).isNull();
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.userStatus()).isSameAs(UserStatus.DELETED);
    }

    private User createUser(String email) {
        return User.of(email, "password1!", true);
    }
}