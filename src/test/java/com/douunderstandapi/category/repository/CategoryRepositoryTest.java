package com.douunderstandapi.category.repository;

import com.douunderstandapi.category.domain.Category;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByName() {
        String name = "java";
        Category category = new Category(name);
        categoryRepository.save(category);

        Optional<Category> optionalCategory = categoryRepository.findByName(name);

        assert optionalCategory.isPresent();
        Assertions.assertThat(optionalCategory.get().getName()).isEqualTo(name);
    }
}