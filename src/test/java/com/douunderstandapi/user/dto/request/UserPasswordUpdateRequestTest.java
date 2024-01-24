package com.douunderstandapi.user.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserPasswordUpdateRequestTest {

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
    void validate_invalid_password_null_blank_size_regex() {
        String empty = "";
        String blank = " ";
        String shortPassword = "pass1!";
        String password = "password1!";
        String invalidRegexPassword = "password1";

        UserPasswordUpdateRequest request1 = new UserPasswordUpdateRequest(null);
        UserPasswordUpdateRequest request2 = new UserPasswordUpdateRequest(empty);
        UserPasswordUpdateRequest request3 = new UserPasswordUpdateRequest(blank);
        UserPasswordUpdateRequest request4 = new UserPasswordUpdateRequest(shortPassword);
        UserPasswordUpdateRequest request5 = new UserPasswordUpdateRequest(invalidRegexPassword);
        UserPasswordUpdateRequest successRequest = new UserPasswordUpdateRequest(password);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserPasswordUpdateRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<UserPasswordUpdateRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<UserPasswordUpdateRequest>> result3 =
                validator.validate(request3);
        Set<ConstraintViolation<UserPasswordUpdateRequest>> result4 =
                validator.validate(request4);
        Set<ConstraintViolation<UserPasswordUpdateRequest>> result5 =
                validator.validate(request5);
        Set<ConstraintViolation<UserPasswordUpdateRequest>> result6 =
                validator.validate(successRequest);

        assertThat(result1.size()).isEqualTo(1);
        // not empty & size & reg violation
        assertThat(result2.size()).isEqualTo(3);
        // not blank & size & reg violation
        assertThat(result3.size()).isEqualTo(3);
        assertThat(result4.size()).isEqualTo(1);
        assertThat(result5.size()).isEqualTo(1);
        assertThat(result6.size()).isEqualTo(0);
    }
}