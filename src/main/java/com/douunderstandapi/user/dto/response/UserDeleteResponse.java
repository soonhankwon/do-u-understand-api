package com.douunderstandapi.user.dto.response;

import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.enumType.UserStatus;

public record UserDeleteResponse(
        Long id,
        String email,
        UserStatus userStatus
) {

    public static UserDeleteResponse of(Long id, String email, UserStatus userStatus) {
        return new UserDeleteResponse(id, email, userStatus);
    }

    public static UserDeleteResponse from(User user) {
        return new UserDeleteResponse(
                user.getId(),
                user.getEmail(),
                user.getUserStatus());
    }
}
