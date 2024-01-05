package com.douunderstandapi.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.domain.dto.request.UserAddRequest;
import com.douunderstandapi.user.domain.dto.response.UserAddResponse;
import com.douunderstandapi.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("유저 가입 - 서비스 로직 테스트")
    void addUser() {
        when(userRepository.save(any(User.class))).thenReturn(createUser());
        String email = "test@gmail.com";
        String password = "password1!";

        UserAddRequest request = new UserAddRequest(email, password, true);
        UserAddResponse response = userService.addUser(request);

        assertThat(response.email()).isEqualTo(email);
    }

    private User createUser() {
        return User.of("test@gmail.com", "password1!", true);
    }
}