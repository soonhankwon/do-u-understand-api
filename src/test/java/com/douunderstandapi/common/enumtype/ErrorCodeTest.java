package com.douunderstandapi.common.enumtype;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ErrorCodeTest {

    @Test
    @DisplayName("getter 테스트")
    void getter() {
        ErrorCode unknown = ErrorCode.UNKNOWN;
        assertThat(unknown.getCode()).isEqualTo(5000);
        assertThat(unknown.getMsg()).isEqualTo("서버 내부 에러가 발생했습니다.");
    }
}