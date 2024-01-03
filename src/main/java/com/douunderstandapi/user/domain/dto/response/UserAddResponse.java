package com.douunderstandapi.user.domain.dto.response;

import com.douunderstandapi.user.domain.User;

public record UserAddResponse(
        Long id,
        String email
) {
    public static UserAddResponse of(
            Long id,
            String email) {
        return new UserAddResponse(id, email);
    }

    public static UserAddResponse from(User user) {
        return UserAddResponse.of(
                user.getId(),
                user.getEmail());
    }
}
