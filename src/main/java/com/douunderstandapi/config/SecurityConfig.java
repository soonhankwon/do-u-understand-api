package com.douunderstandapi.config;

import com.douunderstandapi.common.security.dsl.JwtFilterConfig;
import com.douunderstandapi.common.security.handler.CustomAuthenticationEntryPoint;
import com.douunderstandapi.common.security.handler.CustomLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilterConfig jwtFilterConfig;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPointHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/users/auth-email/**",
            "/api/v1/users/**",
            "/h2-console/**",
            "/index.html"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.apply(jwtFilterConfig);

        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .headers(header -> header
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                        .logoutUrl("/api/v1/logout")
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        exception -> exception.authenticationEntryPoint(customAuthenticationEntryPointHandler))
                .build();
    }
}
