package com.douunderstandapi.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserPasswordUpdateRequest(
        @NotBlank(message = "null 또는 공백문자를 허용하지 않습니다.")
        @Size(min = 8, message = "패스워드는 최소 8글자 이상이어야 합니다.")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).*$",
                message = "패스워드는 숫자, 문자, 특수 문자(@#$%^&+=!)를 포함해야 합니다."
        )
        String password
) {
}
