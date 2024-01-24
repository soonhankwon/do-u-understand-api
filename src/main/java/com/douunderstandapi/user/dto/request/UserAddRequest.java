package com.douunderstandapi.user.dto.request;

import com.douunderstandapi.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.function.Function;

public record UserAddRequest(
        @NotBlank(message = "null 또는 공백문자를 허용하지 않습니다.")
        @Email(message = "잘못된 형식의 이메일 입니다.")
        String email,

        @NotBlank(message = "null 또는 공백문자를 허용하지 않습니다.")
        @Size(min = 8, message = "패스워드는 최소 8글자 이상이어야 합니다.")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).*$",
                message = "패스워드는 숫자, 문자, 특수 문자를 포함해야 합니다."
        )
        String password,

        @NotBlank(message = "인증코드는 null 또는 공백문자를 허용하지 않습니다.")
        String code,

        @NotNull(message = "수신여부는 null을 허용하지 않습니다.")
        Boolean isAllowedNotification
) {

    public User toEntity(Function<String, String> encodedFunction) {
        return User.of(email, encodedFunction.apply(this.password), isAllowedNotification);
    }
}
