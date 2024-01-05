package com.douunderstandapi.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.douunderstandapi.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// @Convert의 Converter를 DataJpaTest 시 로드하지 않아 @SpringBootTest로 테스트 진행
@SpringBootTest
@Transactional
@DisplayName(value = "유저 레포지토리 테스트")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void findByEmail() {
        String email = "test@gmail.com";
        User user = createUser();
        userRepository.save(user);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        optionalUser.ifPresent(
                u -> assertThat(u).isSameAs(user)
        );
    }

    @Test
    void findAllByIsAllowedNotification() {
        User user1 = createUser();
        User user2 = createUser();
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> usersByAllowedNotification = userRepository.findAllByIsAllowedNotification(true);

        assertThat(usersByAllowedNotification.size()).isEqualTo(2);
    }

    private User createUser() {
        return User.of("test@gmail.com", "password1!", true);
    }
}