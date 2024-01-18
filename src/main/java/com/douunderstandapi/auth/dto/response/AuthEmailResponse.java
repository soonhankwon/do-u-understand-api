package com.douunderstandapi.auth.dto.response;

public record AuthEmailResponse(
        String code
) {
    public static AuthEmailResponse from(String code) {
        return new AuthEmailResponse(code);
    }
}
