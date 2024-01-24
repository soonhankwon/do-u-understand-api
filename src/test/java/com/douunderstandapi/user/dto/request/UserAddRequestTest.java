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

class UserAddRequestTest {

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
    void validate_is_allowed_notification_null() {
        String email = "test@gmail.com";
        String password = "password1!";
        String code = UUID.randomUUID().toString();

        UserAddRequest request1 = new UserAddRequest(email, password, code, null);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserAddRequest>> result1 =
                validator.validate(request1);

        assertThat(result1.size()).isEqualTo(1);
    }

    @Test
    void validate_email_not_null_blank() {
        String empty = "";
        String blank = " ";
        String password = "password1!";
        String code = UUID.randomUUID().toString();

        UserAddRequest request1 = new UserAddRequest(empty, password, code, true);
        UserAddRequest request2 = new UserAddRequest(blank, password, code, true);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserAddRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<UserAddRequest>> result2 =
                validator.validate(request2);

        assertThat(result1.size()).isEqualTo(1);
        // email 형식 & not blank violation
        assertThat(result2.size()).isEqualTo(2);
    }

    @Test
    void validate_invalid_password_null_blank_size_regex() {
        String email = "test@gmail.com";
        String empty = "";
        String blank = " ";
        String shortPassword = "pass1!";
        String password = "password1!";
        String invalidRegexPassword = "password1";
        String code = UUID.randomUUID().toString();

        UserAddRequest nullRequest = new UserAddRequest(email, null, code, true);
        UserAddRequest request1 = new UserAddRequest(email, empty, code, true);
        UserAddRequest request2 = new UserAddRequest(email, blank, code, true);
        UserAddRequest request3 = new UserAddRequest(email, shortPassword, code, true);
        UserAddRequest request4 = new UserAddRequest(email, invalidRegexPassword, code, true);
        UserAddRequest request5 = new UserAddRequest(email, password, code, true);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserAddRequest>> nullResult =
                validator.validate(nullRequest);
        Set<ConstraintViolation<UserAddRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<UserAddRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<UserAddRequest>> result3 =
                validator.validate(request3);
        Set<ConstraintViolation<UserAddRequest>> result4 =
                validator.validate(request4);
        Set<ConstraintViolation<UserAddRequest>> result5 =
                validator.validate(request5);

        assertThat(nullResult.size()).isEqualTo(1);
        // not empty & size & reg violation
        assertThat(result1.size()).isEqualTo(3);
        // not blank & size & reg violation
        assertThat(result2.size()).isEqualTo(3);
        assertThat(result3.size()).isEqualTo(1);
        assertThat(result4.size()).isEqualTo(1);
        assertThat(result4.size()).isEqualTo(1);
        assertThat(result5.size()).isEqualTo(0);
    }

    @Test
    void validate_code_not_null_blank() {
        String email = "test@gmail.com";
        String empty = "";
        String blank = " ";
        String password = "password1!";
        String code = UUID.randomUUID().toString();

        UserAddRequest request1 = new UserAddRequest(email, password, empty, true);
        UserAddRequest request2 = new UserAddRequest(email, password, blank, true);
        UserAddRequest request3 = new UserAddRequest(email, password, null, true);
        UserAddRequest request4 = new UserAddRequest(email, password, code, true);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserAddRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<UserAddRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<UserAddRequest>> result3 =
                validator.validate(request3);
        Set<ConstraintViolation<UserAddRequest>> result4 =
                validator.validate(request4);

        assertThat(result1.size()).isEqualTo(1);
        assertThat(result2.size()).isEqualTo(1);
        assertThat(result3.size()).isEqualTo(1);
        assertThat(result4.size()).isEqualTo(0);
    }
}