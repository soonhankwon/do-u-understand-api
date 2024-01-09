package com.douunderstandapi.common.security.filter;

import com.douunderstandapi.common.security.dto.UsernamePassword;
import com.douunderstandapi.common.security.impl.UserDetailsImpl;
import com.douunderstandapi.common.utils.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private static final String JWT_PREFIX = "Bearer ";

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        UsernamePassword usernamePassword = objectMapper.readValue(request.getInputStream(), UsernamePassword.class);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                usernamePassword.username(), usernamePassword.password());

        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        Long id = userDetails.getId();
        String email = userDetails.getUsername();

        String accessToken = jwtProvider.createAccessToken(id, email);
        response.setHeader(HttpHeaders.AUTHORIZATION, JWT_PREFIX + accessToken);

        String refreshToken = jwtProvider.createRefreshToken(email);
        response.setHeader("Refresh", JWT_PREFIX + refreshToken);
    }
}
