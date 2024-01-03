package com.douunderstandapi.common.dto.response;

import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import lombok.Builder;
import org.springframework.http.ResponseEntity;

@Builder
public record ErrorResponse(int code, String msg) {

    public static ResponseEntity<ErrorResponse> toResponseEntity(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity.status(ex.getStatus())
                .body(ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .msg(errorCode.getMsg())
                        .build());
    }
}