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

class CommentAddRequestTest {
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
    void validate_post_id_not_null_blank() {
        Long postId = 1L;
        String content = "content";

        CommentAddRequest request1 = new CommentAddRequest(null, content);
        CommentAddRequest request2 = new CommentAddRequest(postId, content);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CommentAddRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<CommentAddRequest>> result2 =
                validator.validate(request2);

        assertThat(result1.size()).isEqualTo(1);
        assertThat(result2.size()).isEqualTo(0);
    }

    @Test
    void validate_content_not_blank() {
        Long postId = 1L;
        String content = "content";
        String empty = "";
        String blank = " ";

        CommentAddRequest request1 = new CommentAddRequest(postId, null);
        CommentAddRequest request2 = new CommentAddRequest(postId, empty);
        CommentAddRequest request3 = new CommentAddRequest(postId, blank);
        CommentAddRequest request4 = new CommentAddRequest(postId, content);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CommentAddRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<CommentAddRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<CommentAddRequest>> result3 =
                validator.validate(request3);
        Set<ConstraintViolation<CommentAddRequest>> result4 =
                validator.validate(request4);

        assertThat(result1.size()).isEqualTo(1);
        assertThat(result2.size()).isEqualTo(1);
        assertThat(result3.size()).isEqualTo(1);
        assertThat(result4.size()).isEqualTo(0);
    }
}