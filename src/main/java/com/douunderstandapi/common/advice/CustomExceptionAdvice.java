package com.douunderstandapi.common.advice;

import com.douunderstandapi.common.dto.response.ErrorResponse;
import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(RedisConnectionFailureException.class)
    protected ResponseEntity<ErrorResponse> handleRedisConnectionException(RedisConnectionFailureException ex) {
        log.warn(
                String.format(
                        "http-status={%d} msg={%s}",
                        500, ex.getMessage()
                )
        );
        return ErrorResponse.toResponseEntity(ErrorCode.REDIS_CONNECTION_DOWN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        return ErrorResponse.toResponseEntity(ex.getBindingResult());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ErrorResponse.toResponseEntity(ex.getMessage());
    }
}
