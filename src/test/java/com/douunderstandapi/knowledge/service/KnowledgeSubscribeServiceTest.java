package com.douunderstandapi.knowledge.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.knowledge.domain.Knowledge;
import com.douunderstandapi.knowledge.repository.KnowledgeRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KnowledgeSubscribeServiceTest {

    @Mock
    private KnowledgeRepository knowledgeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private KnowledgeSubscribeService knowledgeSubscribeService;

    @Test
    @DisplayName("지식 구독 업데이트 - 서비스 로직 테스트")
    void updateKnowledgeSubscribe() {
        String email = "test@gmail.com";
        when(knowledgeRepository.findById(any(Long.class))).thenReturn(Optional.of(createKnowledge()));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(createUser()));

        assertThatThrownBy(() -> knowledgeSubscribeService.updateKnowledgeSubscribe(
                email, 1L, true))
                .isInstanceOf(CustomException.class);
    }

    private Knowledge createKnowledge() {
        return Knowledge.of("함수 네이밍 룰 컨벤션",
                "GET 요청을 처리하는 메서드의 네이밍 규칭......",
                "https://sdnksnd/sds123", createUser());
    }

    private User createUser() {
        return User.of("test@gmail.com", "password1!", true);
    }
}