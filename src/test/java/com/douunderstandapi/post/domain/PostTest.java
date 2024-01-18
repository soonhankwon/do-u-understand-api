package com.douunderstandapi.post.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지식 도메인 클래스 단위 테스트")
class PostTest {

    @Test
    @DisplayName("정적 팩터리 메서드 - of")
    void of() {
        String email = "test1@gmail.com";

        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String link = "https://sdnksnd/sds123";

        Post post = Post.of(title, content, link, createUser(email));

        assertThat(post.getContent()).isEqualTo(content);
    }

    @Test
    void validateAccessAuth() {
        String email1 = "test1@gmail.com";
        String email2 = "test2@gmail.com";

        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String link = "https://sdnksnd/sds123";

        Post post = Post.of(title, content, link, createUser(email1));

        assertThatThrownBy(() -> post.validateAccessAuth(createUser(email2)))
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

        Post post = Post.of(title, content, link, mockUser);

        assertThat(post.getId()).isNull();
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getLink()).isEqualTo(link);
        assertThat(post.getUser()).isSameAs(mockUser);
        assertThat(post.getIsUnderstand()).isFalse();
        assertThat(post.getIsSubscribe()).isFalse();
        assertThat(post.getNotificationCount()).isEqualTo(0);
    }

    private User createUser(String email) {
        return User.of(email, "password1!", true);
    }
}