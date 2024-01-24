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

class AuthEmailRequestTest {

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
    void validate_invalid_email_not_blank_and_email() {
        String emptyEmail = "";
        String wrongEmail = "wrong";
        String email = "test@gmail.com";

        AuthEmailRequest request1 = new AuthEmailRequest(emptyEmail);
        AuthEmailRequest request2 = new AuthEmailRequest(wrongEmail);
        AuthEmailRequest request3 = new AuthEmailRequest(null);
        AuthEmailRequest request4 = new AuthEmailRequest(email);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<AuthEmailRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<AuthEmailRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<AuthEmailRequest>> result3 =
                validator.validate(request3);
        Set<ConstraintViolation<AuthEmailRequest>> result4 =
                validator.validate(request4);

        assertThat(result1.size()).isEqualTo(1);
        assertThat(result2.size()).isEqualTo(1);
        assertThat(result3.size()).isEqualTo(1);
        assertThat(result4.size()).isEqualTo(0);
    }
}