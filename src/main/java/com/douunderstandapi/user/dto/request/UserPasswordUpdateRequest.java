package com.douunderstandapi.user.dto.request;

public record UserPasswordUpdateRequest(
        String password,
        String authCode
) {
}
