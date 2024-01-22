package com.douunderstandapi.post.repository;

import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// @Convert의 Converter를 DataJpaTest 시 로드하지 않아 @SpringBootTest로 테스트 진행
@SpringBootTest
@Transactional
@DisplayName("포스트 레포지토리 테스트")
class PostRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    private User createUser(String email, String password) {
        return User.of(email, password, true);
    }
}