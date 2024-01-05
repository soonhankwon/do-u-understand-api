package com.douunderstandapi.knowledge.repository;

import com.douunderstandapi.knowledge.domain.Knowledge;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// @Convert의 Converter를 DataJpaTest 시 로드하지 않아 @SpringBootTest로 테스트 진행
@SpringBootTest
@Transactional
@DisplayName("지식 레포지토리 테스트")
class KnowledgeRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    KnowledgeRepository knowledgeRepository;

    @Test
    void findAllByUserAndIsSubscribe() {
        String email = "test1@gmail.com";
        String password = "password1!";
        User user = createUser(email, password);

        userRepository.save(user);

        Knowledge knowledge1 = createKnowledge(user);
        Knowledge knowledge2 = createKnowledge(user);

        knowledge1.updateSubscribeStatus(true);
        knowledge2.updateSubscribeStatus(true);

        knowledgeRepository.save(knowledge1);
        knowledgeRepository.save(knowledge2);

        List<Knowledge> knowledgesBySubscribe = knowledgeRepository.findAllByUserAndIsSubscribe(user, true);

        Assertions.assertThat(knowledgesBySubscribe.size()).isEqualTo(2);
    }

    private Knowledge createKnowledge(User user) {
        return Knowledge.of("함수 네이밍 룰 컨벤션",
                "GET 요청을 처리하는 메서드의 네이밍 규칭......",
                "https://sdnksnd/sds123",
                user);
    }

    private User createUser(String email, String password) {
        return User.of(email, password, true);
    }
}