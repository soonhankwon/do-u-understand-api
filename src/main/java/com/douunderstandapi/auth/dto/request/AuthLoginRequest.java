package com.douunderstandapi.auth.dto.request;

public record AuthLoginRequest(
        String email,
        String password
) {
}
