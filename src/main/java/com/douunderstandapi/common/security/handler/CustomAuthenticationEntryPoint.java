package com.douunderstandapi.common.security.handler;

import com.douunderstandapi.common.dto.response.ErrorResponse;
import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        Object e = request.getAttribute("exception");
        if (!isCustomException(e)) {
            responseWithJson(response, new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.LOGIN_REQUIRED_FIRST));
            return;
        }
        CustomException customException = convertToCustomException(e);
        responseWithJson(response, customException);
    }

    private boolean isCustomException(Object ex) {
        return ex instanceof CustomException;
    }

    private CustomException convertToCustomException(Object object) {
        return (CustomException) object;
    }

    private void responseWithJson(HttpServletResponse response, CustomException customException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addHeader("Content-Type", "application/json; charset=UTF-8");
        response.setStatus(customException.getStatus().value());
        response.getWriter().write(toJsonResponse(customException));
    }

    private String toJsonResponse(CustomException customException)
            throws JsonProcessingException {
        ErrorResponse response = ErrorResponse.from(customException);
        return objectMapper.writeValueAsString(response);
    }
}
