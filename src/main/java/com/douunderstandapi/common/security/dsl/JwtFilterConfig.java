package com.douunderstandapi.common.security.dsl;

import com.douunderstandapi.common.security.filter.JwtAuthenticationFilter;
import com.douunderstandapi.common.security.filter.JwtVerificationFilter;
import com.douunderstandapi.common.utils.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtFilterConfig extends AbstractHttpConfigurer<JwtFilterConfig, HttpSecurity> {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final AuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    public void configure(HttpSecurity security) throws Exception {
        AuthenticationManager authenticationManager = security.getSharedObject(AuthenticationManager.class);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider, objectMapper);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        security.addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtProvider);
        security.addFilterBefore(jwtVerificationFilter, JwtAuthenticationFilter.class);
    }
}
