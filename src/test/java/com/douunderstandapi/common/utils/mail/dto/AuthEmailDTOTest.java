package com.douunderstandapi.common.utils.mail.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class AuthEmailDTOTest {

    @Test
    void from() {
        Supplier<String> randomCodeFunction = () -> UUID.randomUUID().toString();

        AuthEmailDTO dto = AuthEmailDTO.from(randomCodeFunction);
        assertThat(dto.code()).isNotNull();
        assertThat(dto.title()).isEqualTo("이메일 인증");
    }
}