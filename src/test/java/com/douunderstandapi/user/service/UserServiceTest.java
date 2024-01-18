package com.douunderstandapi.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.douunderstandapi.common.utils.mail.EmailUtils;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.dto.request.UserAddRequest;
import com.douunderstandapi.user.dto.response.UserAddResponse;
import com.douunderstandapi.user.dto.response.UserDeleteResponse;
import com.douunderstandapi.user.enumType.UserStatus;
import com.douunderstandapi.user.repository.UserRepository;
import com.douunderstandapi.user.repository.redis.UserEmailAuthCodeRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserEmailAuthCodeRepository userEmailAuthCodeRepository;

    @Mock
    private EmailUtils emailUtils;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("유저 가입 - 서비스 로직 테스트")
    void addUser() {
        String code = UUID.randomUUID().toString();
        String email = "test@gmail.com";
        String password = "password1!";

        when(passwordEncoder.encode(anyString())).thenReturn(password);
        when(userRepository.save(any(User.class))).thenReturn(createUser());
        when(userEmailAuthCodeRepository.get(email)).thenReturn(
                Optional.of(code));

        UserAddRequest request = new UserAddRequest(email, password, code, true);
        UserAddResponse response = userService.addUser(request);

        assertThat(response.email()).isEqualTo(email);
    }

    @Test
    @DisplayName("유저 이메일 인증 - 서비스 로직 테스트")
    void authUserEmail() {
        String code = UUID.randomUUID().toString();
        String email = "test@gmail.com";

        when(emailUtils.sendEmailAuthMessage(anyString())).thenReturn(code);
        doNothing().when(userEmailAuthCodeRepository).put(anyString(), anyString());

        UserEmailAuthRequest request = new UserEmailAuthRequest(email);
        UserEmailAuthResponse response = userService.authUserEmail(request);

        assertThat(response.code()).isEqualTo(code);
    }

    @Test
    @DisplayName("유저 탈퇴 - 소프트 삭제 테스트")
    void deleteUser_soft_delete() {
        String email = "test@gmail.com";

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(createUser()));

        UserDeleteResponse response = userService.deleteUser(email);

        assertThat(response.userStatus()).isSameAs(UserStatus.DELETED);
    }

    private User createUser() {
        return User.of("test@gmail.com", "password1!", true);
    }
}