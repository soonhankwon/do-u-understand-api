package com.douunderstandapi.user.domain.dto.request;

import com.douunderstandapi.user.domain.User;

public record UserAddRequest(
        String email,
        String password,
        Boolean isAllowedNotification
) {

    public User toEntity() {
        return User.of(email, password, isAllowedNotification);
    }
}
