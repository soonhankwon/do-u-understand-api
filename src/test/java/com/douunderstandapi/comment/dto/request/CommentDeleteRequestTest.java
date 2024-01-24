package com.douunderstandapi.comment.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentDeleteRequestTest {
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
    void validate_comment_id_not_null_blank() {
        Long commentId = 1L;

        CommentDeleteRequest request1 = new CommentDeleteRequest(commentId);
        CommentDeleteRequest request2 = new CommentDeleteRequest(null);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CommentDeleteRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<CommentDeleteRequest>> result2 =
                validator.validate(request2);

        assertThat(result1.size()).isEqualTo(0);
        assertThat(result2.size()).isEqualTo(1);
    }
}