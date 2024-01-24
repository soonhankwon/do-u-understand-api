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

class AuthLoginRequestTest {
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
    void validate_invalid_email_password() {
        String email = "test@gmail.com";
        String password = "password1!";

        AuthLoginRequest request1 = new AuthLoginRequest(email, password);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<AuthLoginRequest>> result1 =
                validator.validate(request1);

        assertThat(result1.size()).isEqualTo(0);
    }

    @Test
    void validate_invalid_email_null_or_empty_regex() {
        String empty = "";
        String blank = " ";
        String password = "password1!";
        String wrongEmail = "wrong";

        AuthLoginRequest request1 = new AuthLoginRequest(empty, password);
        AuthLoginRequest request2 = new AuthLoginRequest(blank, password);
        AuthLoginRequest request3 = new AuthLoginRequest(wrongEmail, password);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<AuthLoginRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<AuthLoginRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<AuthLoginRequest>> result3 =
                validator.validate(request3);

        assertThat(result1.size()).isEqualTo(1);
        assertThat(result2.size()).isEqualTo(2);
        assertThat(result3.size()).isEqualTo(1);
    }

    @Test
    void validate_invalid_password_null_blank_size_regex() {
        String email = "test@gmail.com";
        String empty = "";
        String blank = " ";
        String shortPassword = "pass1!";
        String password = "password1!";
        String invalidRegexPassword = "password1";

        AuthLoginRequest request1 = new AuthLoginRequest(email, empty);
        AuthLoginRequest request2 = new AuthLoginRequest(email, blank);
        AuthLoginRequest request3 = new AuthLoginRequest(email, null);
        AuthLoginRequest request4 = new AuthLoginRequest(email, shortPassword);
        AuthLoginRequest request5 = new AuthLoginRequest(email, invalidRegexPassword);
        AuthLoginRequest request6 = new AuthLoginRequest(email, password);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<AuthLoginRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<AuthLoginRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<AuthLoginRequest>> result3 =
                validator.validate(request3);
        Set<ConstraintViolation<AuthLoginRequest>> result4 =
                validator.validate(request4);
        Set<ConstraintViolation<AuthLoginRequest>> result5 =
                validator.validate(request5);
        Set<ConstraintViolation<AuthLoginRequest>> result6 =
                validator.validate(request6);

        assertThat(result1.size()).isEqualTo(3);
        assertThat(result2.size()).isEqualTo(3);
        assertThat(result3.size()).isEqualTo(1);
        assertThat(result4.size()).isEqualTo(1);
        assertThat(result5.size()).isEqualTo(1);
        assertThat(result6.size()).isEqualTo(0);
    }
}