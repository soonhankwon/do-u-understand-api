package com.douunderstandapi.common.utils.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("JWT Provider 테스트")
@SpringBootTest(classes = {JwtProvider.class})
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void createAccessToken() {
        String accessToken = jwtProvider.createAccessToken(1L, "test@gmail.com");

        assertThat(accessToken).isNotNull();
    }

    @Test
    void createRefreshToken() {
        String refreshToken = jwtProvider.createRefreshToken("test@gmail.com");

        assertThat(refreshToken).isNotNull();
    }

    @Test
    void getClaims() {
        String email = "test@gmail.com";
        String accessToken = jwtProvider.createAccessToken(1L, email);
        Claims claims = jwtProvider.getClaims(accessToken);

        assertThat(claims.getSubject()).isEqualTo(email);
    }
}