package com.douunderstandapi.common.utils.mail.dto;

public record PasswordRefreshEmailDTO(
        String title,
        String code
) {
    public static PasswordRefreshEmailDTO from(String password) {
        return new PasswordRefreshEmailDTO("임시 패스워드 발급", password);
    }
}
