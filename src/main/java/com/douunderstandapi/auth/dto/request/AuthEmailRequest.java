package com.douunderstandapi.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthEmailRequest(
        @Email(message = "잘못된 형식의 이메일입니다.")
        @NotBlank(message = "null 또는 공백문자를 허용하지 않습니다.")
        String email
) {
}
