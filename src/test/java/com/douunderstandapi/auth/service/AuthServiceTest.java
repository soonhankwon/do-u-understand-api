package com.douunderstandapi.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.douunderstandapi.auth.dto.request.AuthEmailRequest;
import com.douunderstandapi.auth.dto.request.AuthLoginRequest;
import com.douunderstandapi.auth.dto.response.AuthEmailResponse;
import com.douunderstandapi.auth.dto.response.AuthLoginResponse;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.common.utils.jwt.JwtProvider;
import com.douunderstandapi.common.utils.mail.EmailUtils;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import java.time.Instant;
import java.util.Date;
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

    @Mock
    private EmailUtils emailUtils;

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
    @DisplayName("토큰 재발급 - 헤더에 새로운 AccessToken 검증")
    void refresh() {
        String email = "test@gmail.com";
        String password = "password1!";
        User user = User.of(email, password, true);

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        mockHttpServletRequest.setCookies(new Cookie("refresh_token", UUID.randomUUID().toString()));

        when(jwtProvider.getClaims(anyString()))
                .thenReturn(createClaims(email));
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));
        String accessToken = UUID.randomUUID().toString();
        when(jwtProvider.createAccessToken(any(), anyString()))
                .thenReturn(accessToken);

        AuthLoginResponse response = authService.refresh(mockHttpServletRequest, mockHttpServletResponse);

        assertThat(response.email()).isEqualTo(email);
        assertThat(Objects.requireNonNull(mockHttpServletResponse.getHeader(HttpHeaders.AUTHORIZATION)))
                .isEqualTo("Bearer " + accessToken);
    }

    @Test
    @DisplayName("토큰 재발급 - request의 Cookies 배열이 null일 경우 예외처리")
    void refresh_exception_cookie_is_null() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        assertThatThrownBy(() -> authService.refresh(mockHttpServletRequest, mockHttpServletResponse))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void authEmail() {
        String email = "test@gmail.com";
        AuthEmailRequest request = new AuthEmailRequest(email);
        String mockCode = UUID.randomUUID().toString();
        when(emailUtils.sendEmailAuthMessage(anyString()))
                .thenReturn(mockCode);

        AuthEmailResponse response = authService.authEmail(request);

        assertThat(response.code()).isEqualTo(mockCode);
    }

    @Test
    void getClaims() {
        String email = "test@gmail.com";
        String accessToken = UUID.randomUUID().toString();
        when(jwtProvider.getClaims(accessToken))
                .thenReturn(createClaims(email));

        Claims claims = authService.getClaims(accessToken);

        assertThat(claims.getSubject()).isEqualTo(email);
        assertThat(claims.getExpiration()).isAfter(Date.from(Instant.now()));
    }

    private Claims createClaims(String email) {
        return Jwts.claims()
                .id("jtl")
                .subject(email)
                .expiration(Date.from(Instant.now().plusSeconds(60 * 60 * 24 * 3)))
                .issuer("test")
                .build();
    }
}