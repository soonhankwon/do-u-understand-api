package com.douunderstandapi.category.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void getter() {
        String categoryName = "java";
        Category category = new Category(categoryName);

        assertThat(category.getId()).isNull();
        assertThat(category.getName()).isEqualTo(categoryName);
    }
}