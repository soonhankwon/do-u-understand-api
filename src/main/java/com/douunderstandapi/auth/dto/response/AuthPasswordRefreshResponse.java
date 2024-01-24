package com.douunderstandapi.auth.dto.response;

public record AuthPasswordRefreshResponse(
        String code
) {
    public static AuthPasswordRefreshResponse of(String code) {
        return new AuthPasswordRefreshResponse(code);
    }
}
