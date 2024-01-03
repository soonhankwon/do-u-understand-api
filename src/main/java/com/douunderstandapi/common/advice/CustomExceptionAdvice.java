package com.douunderstandapi.common.advice;

import com.douunderstandapi.common.dto.response.ErrorResponse;
import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionAdvice {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustom400Exception(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn(
                String.format(
                        "http-status={%d} code={%d} msg={%s}",
                        ex.getStatus().value(), errorCode.getCode(), errorCode.getMsg()
                )
        );
        return ErrorResponse.toResponseEntity(ex);
    }
}
