package com.douunderstandapi.user.dto.response;

import com.douunderstandapi.user.domain.User;

public record UserAddResponse(
        Long id,
        String email,
        Boolean isAllowedNotification
) {
    public static UserAddResponse of(
            Long id,
            String email,
            Boolean isAllowedNotification) {
        return new UserAddResponse(id, email, isAllowedNotification);
    }

    public static UserAddResponse from(User user) {
        return UserAddResponse.of(
                user.getId(),
                user.getEmail(),
                user.getIsAllowedNotification());
    }
}
