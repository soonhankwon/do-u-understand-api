package com.douunderstandapi.common.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ErrorResponseTest {

    @Test
    void toResponseEntity() {
        CustomException customException = new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_AUTH_CODE);
        ResponseEntity<ErrorResponse> responseEntity = ErrorResponse.toResponseEntity(customException);

        assertThat(responseEntity.getStatusCode()).isSameAs(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getter_record() {
        int code = 400;
        String msg = "Bad Request";
        ErrorResponse badRequest = ErrorResponse.builder()
                .code(code)
                .msg(msg)
                .build();

        assertThat(badRequest.code()).isEqualTo(code);
        assertThat(badRequest.msg()).isEqualTo(msg);
    }
}