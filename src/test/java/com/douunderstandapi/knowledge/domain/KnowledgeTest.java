package com.douunderstandapi.knowledge.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지식 도메인 클래스 단위 테스트")
class KnowledgeTest {

    @Test
    @DisplayName("정적 팩터리 메서드 - of")
    void of() {
        String email = "test1@gmail.com";

        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String link = "https://sdnksnd/sds123";

        Knowledge knowledge = Knowledge.of(title, content, link, createUser(email));

        assertThat(knowledge.getContent()).isEqualTo(content);
    }

    @Test
    void validateAccessAuth() {
        String email1 = "test1@gmail.com";
        String email2 = "test2@gmail.com";

        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String link = "https://sdnksnd/sds123";

        Knowledge knowledge = Knowledge.of(title, content, link, createUser(email1));

        assertThatThrownBy(() -> knowledge.validateAccessAuth(createUser(email2)))
                .isInstanceOf(CustomException.class);

    }

    @Test
    @DisplayName("getter 테스트")
    void getter() {
        String email = "test1@gmail.com";

        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String link = "https://sdnksnd/sds123";
        User mockUser = createUser(email);

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

    private User createUser(String email) {
        return User.of(email, "password1!", true);
    }
}