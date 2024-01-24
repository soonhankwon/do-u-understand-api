package com.douunderstandapi.post.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostUpdateRequestTest {
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
    void validate_title_not_null_blank() {
        String title = "title";
        String content = "content";
        String link = "https://blahblah.xyz";
        String categoryName = "java";
        String empty = "";
        String blank = " ";

        PostUpdateRequest request1 = new PostUpdateRequest(null, content, link, categoryName);
        PostUpdateRequest request2 = new PostUpdateRequest(empty, content, link, categoryName);
        PostUpdateRequest request3 = new PostUpdateRequest(blank, content, link, categoryName);
        PostUpdateRequest request4 = new PostUpdateRequest(title, content, link, categoryName);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<PostUpdateRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<PostUpdateRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<PostUpdateRequest>> result3 =
                validator.validate(request3);
        Set<ConstraintViolation<PostUpdateRequest>> result4 =
                validator.validate(request4);

        assertThat(result1.size()).isEqualTo(1);
        assertThat(result2.size()).isEqualTo(1);
        assertThat(result3.size()).isEqualTo(1);
        assertThat(result4.size()).isEqualTo(0);
    }

    @Test
    void validate_content_not_null_blank() {
        String title = "title";
        String link = "https://blahblah.xyz";
        String categoryName = "java";
        String empty = "";
        String blank = " ";

        PostUpdateRequest request1 = new PostUpdateRequest(title, null, link, categoryName);
        PostUpdateRequest request2 = new PostUpdateRequest(title, empty, link, categoryName);
        PostUpdateRequest request3 = new PostUpdateRequest(title, blank, link, categoryName);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<PostUpdateRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<PostUpdateRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<PostUpdateRequest>> result3 =
                validator.validate(request3);

        assertThat(result1.size()).isEqualTo(1);
        assertThat(result2.size()).isEqualTo(1);
        assertThat(result3.size()).isEqualTo(1);
    }

    @Test
    void validate_link_not_null() {
        String title = "title";
        String content = "content";
        String categoryName = "java";
        String empty = "";
        String blank = " ";

        PostUpdateRequest request1 = new PostUpdateRequest(title, content, null, categoryName);
        PostUpdateRequest request2 = new PostUpdateRequest(title, content, empty, categoryName);
        PostUpdateRequest request3 = new PostUpdateRequest(title, content, blank, categoryName);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<PostUpdateRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<PostUpdateRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<PostUpdateRequest>> result3 =
                validator.validate(request3);

        assertThat(result1.size()).isEqualTo(1);
        assertThat(result2.size()).isEqualTo(0);
        assertThat(result3.size()).isEqualTo(0);
    }

    @Test
    void validate_category_name_not_null_blank() {
        String title = "title";
        String content = "content";
        String link = "https://blahblah.xyz";
        String empty = "";
        String blank = " ";

        PostUpdateRequest request1 = new PostUpdateRequest(title, content, link, null);
        PostUpdateRequest request2 = new PostUpdateRequest(title, content, link, empty);
        PostUpdateRequest request3 = new PostUpdateRequest(title, content, link, blank);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<PostUpdateRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<PostUpdateRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<PostUpdateRequest>> result3 =
                validator.validate(request3);

        assertThat(result1.size()).isEqualTo(1);
        assertThat(result2.size()).isEqualTo(1);
        assertThat(result3.size()).isEqualTo(1);
    }
}