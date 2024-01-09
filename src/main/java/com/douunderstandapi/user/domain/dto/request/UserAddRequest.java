package com.douunderstandapi.user.domain.dto.request;

import com.douunderstandapi.user.domain.User;
import java.util.function.Function;

public record UserAddRequest(
        String email,
        String password,
        String code,
        Boolean isAllowedNotification
) {

    public User toEntity(Function<String, String> encodedFunction) {
        return User.of(email, encodedFunction.apply(this.password), isAllowedNotification);
    }
}
