package com.douunderstandapi.auth.service;

import com.douunderstandapi.auth.dto.request.AuthEmailRequest;
import com.douunderstandapi.auth.dto.request.AuthLoginRequest;
import com.douunderstandapi.auth.dto.response.AuthEmailResponse;
import com.douunderstandapi.auth.dto.response.AuthLoginResponse;
import com.douunderstandapi.auth.repository.redis.AuthEmailCodeRepository;
import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.common.utils.jwt.JwtProvider;
import com.douunderstandapi.common.utils.mail.EmailUtils;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 7;
    private static final String REFRESH_TOKEN_NAME = "refresh_token";
    private static final String JWT_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtils emailUtils;
    private final AuthEmailCodeRepository authEmailCodeRepository;

    public AuthLoginResponse login(AuthLoginRequest request, HttpServletResponse httpServletResponse) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        user.validateStatus();
        user.validatePassword(request.password(), passwordEncoder::matches);

        String email = user.getEmail();
        String accessTokenValue = jwtProvider.createAccessToken(user.getId(), email);
        String accessToken = JWT_PREFIX + accessTokenValue;
        httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, accessToken);

        String refreshToken = jwtProvider.createRefreshToken(email);
        ResponseCookie cookie = createCookie(refreshToken, COOKIE_MAX_AGE);

        httpServletResponse.setHeader("Set-Cookie", cookie.toString());
        return new AuthLoginResponse(user.getId(), email, accessToken);
    }

    public Boolean logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Cookie cookie = Arrays.stream(httpServletRequest.getCookies())
                .filter(c -> c.getName().equals("refresh_token"))
                .findFirst()
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_COOKIE));
        String refreshToken = cookie.getValue();
        ResponseCookie expiredCookie = createCookie(refreshToken, 0);
        httpServletResponse.setHeader("Set-Cookie", expiredCookie.toString());
        return Boolean.TRUE;
    }

    public AuthLoginResponse refresh(HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse) {

        if (httpServletRequest.getCookies() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_COOKIE);
        }

        String refreshToken = getCookieFromHttpServletRequest(httpServletRequest);
        Claims claims = jwtProvider.getClaims(refreshToken);
        String email = claims.getSubject();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Date now = Date.from(Instant.now());
        if (claims.getExpiration().before(now)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.COOKIE_EXPIRATION);
        }

        String newAccessTokenValue = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        String newAccessToken = JWT_PREFIX + newAccessTokenValue;
        httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, newAccessToken);

        return new AuthLoginResponse(user.getId(), email, newAccessToken);
    }

    public AuthEmailResponse authEmail(AuthEmailRequest request) {
        String email = request.email();
        String code = emailUtils.sendEmailAuthMessage(email);

        // 해당 이메일의 인증코드가 이미 존재한다면 캐시 삭제하고 재발급 & 캐싱
        authEmailCodeRepository.put(email, code);
        return AuthEmailResponse.from(code);
    }

    public Claims getClaims(String accessToken) {
        return jwtProvider.getClaims(accessToken);
    }

    private String getCookieFromHttpServletRequest(HttpServletRequest httpServletRequest) {
        Cookie cookie = Arrays.stream(httpServletRequest.getCookies())
                .filter(c -> c.getName().equals(REFRESH_TOKEN_NAME))
                .findFirst()
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_COOKIE));
        return cookie.getValue();
    }

    private ResponseCookie createCookie(String refreshToken, int maxAge) {
        return ResponseCookie.from(REFRESH_TOKEN_NAME, refreshToken)
                .maxAge(maxAge)
                .domain("localhost")
                .path("/")
                .secure(true)
                .sameSite(SameSite.NONE.name())
                .httpOnly(true)
                .build();
    }
}
