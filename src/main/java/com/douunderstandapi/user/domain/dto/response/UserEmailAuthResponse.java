package com.douunderstandapi.user.domain.dto.response;

public record UserEmailAuthResponse(
        String code
) {
    public static UserEmailAuthResponse from(String code) {
        return new UserEmailAuthResponse(code);
    }
}
