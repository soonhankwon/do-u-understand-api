package com.douunderstandapi.auth.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthPasswordRefreshRequestTest {
    private ValidatorFactory validatorFactory;

    @BeforeEach
    void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    @AfterEach
    void cleanUp() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }

    @Test
    void validate_invalid_email_null_or_empty_regex() {
        String empty = "";
        String blank = " ";
        String wrongEmail = "wrong";

        AuthPasswordRefreshRequest request1 = new AuthPasswordRefreshRequest(empty);
        AuthPasswordRefreshRequest request2 = new AuthPasswordRefreshRequest(blank);
        AuthPasswordRefreshRequest request3 = new AuthPasswordRefreshRequest(wrongEmail);
        AuthPasswordRefreshRequest request4 = new AuthPasswordRefreshRequest(null);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<AuthPasswordRefreshRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<AuthPasswordRefreshRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<AuthPasswordRefreshRequest>> result3 =
                validator.validate(request3);
        Set<ConstraintViolation<AuthPasswordRefreshRequest>> result4 =
                validator.validate(request4);

        assertThat(result1.size()).isEqualTo(1);
        assertThat(result2.size()).isEqualTo(2);
        assertThat(result3.size()).isEqualTo(1);
        assertThat(result4.size()).isEqualTo(1);
    }
}