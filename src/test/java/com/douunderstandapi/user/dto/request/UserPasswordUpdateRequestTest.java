package com.douunderstandapi.user.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.UUID;
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
        String code = UUID.randomUUID().toString();

        UserPasswordUpdateRequest request1 = new UserPasswordUpdateRequest(null, code);
        UserPasswordUpdateRequest request2 = new UserPasswordUpdateRequest(empty, code);
        UserPasswordUpdateRequest request3 = new UserPasswordUpdateRequest(blank, code);
        UserPasswordUpdateRequest request4 = new UserPasswordUpdateRequest(shortPassword, code);
        UserPasswordUpdateRequest request5 = new UserPasswordUpdateRequest(invalidRegexPassword, code);
        UserPasswordUpdateRequest successRequest = new UserPasswordUpdateRequest(password, code);

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

    @Test
    void validate_code_not_null_blank() {
        String password = "password1!";
        String empty = "";
        String blank = " ";
        String code = UUID.randomUUID().toString();

        UserPasswordUpdateRequest request1 = new UserPasswordUpdateRequest(password, null);
        UserPasswordUpdateRequest request2 = new UserPasswordUpdateRequest(password, empty);
        UserPasswordUpdateRequest request3 = new UserPasswordUpdateRequest(password, blank);
        UserPasswordUpdateRequest request4 = new UserPasswordUpdateRequest(password, code);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserPasswordUpdateRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<UserPasswordUpdateRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<UserPasswordUpdateRequest>> result3 =
                validator.validate(request3);
        Set<ConstraintViolation<UserPasswordUpdateRequest>> result4 =
                validator.validate(request4);

        assertThat(result1.size()).isEqualTo(1);
        // not empty violation
        assertThat(result2.size()).isEqualTo(1);
        // not blank violation
        assertThat(result3.size()).isEqualTo(1);
        assertThat(result4.size()).isEqualTo(0);
    }
}