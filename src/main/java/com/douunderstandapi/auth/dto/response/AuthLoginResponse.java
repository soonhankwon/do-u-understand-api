package com.douunderstandapi.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthLoginResponse(
        Long id,
        String email,
        @JsonProperty(value = "Authorization")
        String authorization
) {
}
