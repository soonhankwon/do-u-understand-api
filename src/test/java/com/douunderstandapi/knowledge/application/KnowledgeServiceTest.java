package com.douunderstandapi.knowledge.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.knowledge.domain.Knowledge;
import com.douunderstandapi.knowledge.domain.dto.request.KnowledgeAddRequest;
import com.douunderstandapi.knowledge.domain.dto.request.KnowledgeUpdateRequest;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeAddResponse;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeGetResponse;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeUpdateResponse;
import com.douunderstandapi.knowledge.infrastructure.repository.KnowledgeRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.infrastructure.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KnowledgeServiceTest {

    @Mock
    private KnowledgeRepository knowledgeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private KnowledgeService knowledgeService;

    @Test
    @DisplayName("지식 생성 - 서비스 로직 테스트")
    void addKnowledge() {
        when(knowledgeRepository.save(any(Knowledge.class))).thenReturn(createKnowledge());
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(createUser()));
        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String email = "test@gmail.com";
        String link = "https://sdnksnd/sds123";

        KnowledgeAddRequest request = new KnowledgeAddRequest(title, content, email, link);

        KnowledgeAddResponse response = knowledgeService.addKnowledge(request);

        assertThat(response.content()).isEqualTo(content);
    }

    @Test
    @DisplayName("지식 생성 - 유저 email 미존재 예외처리")
    void addKnowledge_user_email_not_exist_exception() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String email = "test@gmail.com";
        String link = "https://sdnksnd/sds123";

        KnowledgeAddRequest request = new KnowledgeAddRequest(title, content, email, link);

        assertThatThrownBy(() ->
                knowledgeService.addKnowledge(request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("지식 상세조회(단건) - 서비스 로직 테스트")
    void findKnowledge() {
        when(knowledgeRepository.findById(any(Long.class))).thenReturn(Optional.of(createKnowledge()));

        KnowledgeGetResponse response = knowledgeService.findKnowledge(1L);

        assertThat(response.content()).isEqualTo("GET 요청을 처리하는 메서드의 네이밍 규칭......");
    }

    @Test
    @DisplayName("지식 업데이트 - 서비스 로직 테스트")
    void updateKnowledge() {
        when(knowledgeRepository.findById(any(Long.class))).thenReturn(Optional.of(createKnowledge()));

        KnowledgeUpdateRequest request = new KnowledgeUpdateRequest("RESTful API 수정본", "수정된 버전의 내용",
                "https://mock-link");
        KnowledgeUpdateResponse response = knowledgeService.update(1L, request);

        assertThat(response.content()).isEqualTo("수정된 버전의 내용");
    }

    @Test
    @DisplayName("지식 삭제 - 서비스 로직 테스트")
    void deleteKnowledge() {
        when(knowledgeRepository.findById(any(Long.class))).thenReturn(Optional.of(createKnowledge()));

        String response = knowledgeService.delete(1L);

        assertThat(response).isEqualTo("deleted");
    }

    private Knowledge createKnowledge() {
        return Knowledge.of("함수 네이밍 룰 컨벤션",
                "GET 요청을 처리하는 메서드의 네이밍 규칭......",
                "https://sdnksnd/sds123", createUser());
    }

    private User createUser() {
        return User.of("test@gmail.com", "password1!");
    }
}