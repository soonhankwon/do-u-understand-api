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

class PostAddRequestTest {
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

        PostAddRequest request1 = new PostAddRequest(null, content, link, categoryName);
        PostAddRequest request2 = new PostAddRequest(empty, content, link, categoryName);
        PostAddRequest request3 = new PostAddRequest(blank, content, link, categoryName);
        PostAddRequest request4 = new PostAddRequest(title, content, link, categoryName);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<PostAddRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<PostAddRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<PostAddRequest>> result3 =
                validator.validate(request3);
        Set<ConstraintViolation<PostAddRequest>> result4 =
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

        PostAddRequest request1 = new PostAddRequest(title, null, link, categoryName);
        PostAddRequest request2 = new PostAddRequest(title, empty, link, categoryName);
        PostAddRequest request3 = new PostAddRequest(title, blank, link, categoryName);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<PostAddRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<PostAddRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<PostAddRequest>> result3 =
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

        PostAddRequest request1 = new PostAddRequest(title, content, null, categoryName);
        PostAddRequest request2 = new PostAddRequest(title, content, empty, categoryName);
        PostAddRequest request3 = new PostAddRequest(title, content, blank, categoryName);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<PostAddRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<PostAddRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<PostAddRequest>> result3 =
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

        PostAddRequest request1 = new PostAddRequest(title, content, link, null);
        PostAddRequest request2 = new PostAddRequest(title, content, link, empty);
        PostAddRequest request3 = new PostAddRequest(title, content, link, blank);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<PostAddRequest>> result1 =
                validator.validate(request1);
        Set<ConstraintViolation<PostAddRequest>> result2 =
                validator.validate(request2);
        Set<ConstraintViolation<PostAddRequest>> result3 =
                validator.validate(request3);

        assertThat(result1.size()).isEqualTo(1);
        assertThat(result2.size()).isEqualTo(1);
        assertThat(result3.size()).isEqualTo(1);
    }
}