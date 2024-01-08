package com.douunderstandapi.common.utils.mail.dto;

import java.util.function.Supplier;

public record AuthEmailDTO(
        String title,
        String code
) {
    public static AuthEmailDTO from(Supplier<String> randomCodeFunction) {
        return new AuthEmailDTO("이메일 인증", randomCodeFunction.get());
    }
}
