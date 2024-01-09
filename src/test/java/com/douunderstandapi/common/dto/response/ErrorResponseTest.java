package com.douunderstandapi.common.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import java.util.Objects;
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
    void toResponseEntity_general_exception() {
        ErrorCode generalEx = ErrorCode.REDIS_CONNECTION_DOWN;
        ResponseEntity<ErrorResponse> responseEntity = ErrorResponse.toResponseEntity(generalEx);
        int code = Objects.requireNonNull(responseEntity.getBody()).code();
        String msg = responseEntity.getBody().msg();

        assertThat(responseEntity.getStatusCode()).isSameAs(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(code).isEqualTo(generalEx.getCode());
        assertThat(msg).isEqualTo(generalEx.getMsg());
    }

    @Test
    void from() {
        CustomException customException = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.UNKNOWN);
        ErrorResponse response = ErrorResponse.from(customException);
        String msg = customException.getErrorCode().getMsg();

        assertThat(response.msg()).isEqualTo(msg);
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