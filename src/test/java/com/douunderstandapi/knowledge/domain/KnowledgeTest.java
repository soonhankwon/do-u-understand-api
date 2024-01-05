package com.douunderstandapi.knowledge.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.douunderstandapi.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지식 도메인 클래스 단위 테스트")
class KnowledgeTest {

    @Test
    @DisplayName("정적 팩터리 메서드 - of")
    void of() {
        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String link = "https://sdnksnd/sds123";

        Knowledge knowledge = Knowledge.of(title, content, link, createUser());

        assertThat(knowledge.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("getter 테스트")
    void getter() {
        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String link = "https://sdnksnd/sds123";
        User mockUser = createUser();

        Knowledge knowledge = Knowledge.of(title, content, link, mockUser);

        assertThat(knowledge.getId()).isNull();
        assertThat(knowledge.getTitle()).isEqualTo(title);
        assertThat(knowledge.getContent()).isEqualTo(content);
        assertThat(knowledge.getLink()).isEqualTo(link);
        assertThat(knowledge.getUser()).isSameAs(mockUser);
        assertThat(knowledge.getIsUnderstand()).isFalse();
        assertThat(knowledge.getIsSubscribe()).isFalse();
        assertThat(knowledge.getNotificationCount()).isEqualTo(0);
    }

    private User createUser() {
        return User.of("test@gmail.com", "password1!", true);
    }
}