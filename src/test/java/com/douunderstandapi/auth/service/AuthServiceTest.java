package com.douunderstandapi.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.douunderstandapi.auth.dto.request.AuthLoginRequest;
import com.douunderstandapi.auth.dto.response.AuthLoginResponse;
import com.douunderstandapi.common.utils.jwt.JwtProvider;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("로그인 - 서비스 로직 테스트")
    void login() {
        String email = "test@gmail.com";
        String password = "password1!";
        AuthLoginRequest request = new AuthLoginRequest(email, password);

        User user = User.of(email, password, true);
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        String accessToken = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();
        when(jwtProvider.createAccessToken(any(), anyString()))
                .thenReturn(accessToken);
        when(jwtProvider.createRefreshToken(anyString()))
                .thenReturn(refreshToken);

        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        AuthLoginResponse response = authService.login(request, mockHttpServletResponse);

        assertThat(response.email()).isEqualTo(email);
        assertThat(mockHttpServletResponse.getHeader(HttpHeaders.AUTHORIZATION))
                .isEqualTo("Bearer " + accessToken);
        assertThat(Objects.requireNonNull(mockHttpServletResponse.getCookie("refresh_token")).getValue())
                .isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("로그아웃 - 리프레시 토큰 쿠키 maxAge: 0")
    void logout() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setCookies(new Cookie("refresh_token", UUID.randomUUID().toString()));
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        Boolean isLogout = authService.logout(mockHttpServletRequest, mockHttpServletResponse);

        assertThat(isLogout).isTrue();
        assertThat(Objects.requireNonNull(mockHttpServletResponse.getCookie("refresh_token")).getMaxAge())
                .isEqualTo(0L);
    }

    @Test
    void refresh() {
    }

    @Test
    void authEmail() {
    }

    @Test
    void getClaims() {
    }
}