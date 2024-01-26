package com.douunderstandapi.comment.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.douunderstandapi.category.domain.Category;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.user.domain.User;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName(value = "Comment Domain 유닛 테스트")
class CommentTest {

    @Test
    void validateDeleteAuth() {
        String email = "selfnews@gmail.com";
        String password = "password1!";
        User userByPost = User.of(email, password, true);

        User user = User.of(email, password, true);

        String title = "title";
        String content = "content";
        String link = "link";
        Category category = new Category("java");

        Post post = Post.of(title, content, link, userByPost, category);

        String commentContent = "좋은 게시물입니다!";
        Comment comment = new Comment(commentContent, userByPost, post);

        assertThatThrownBy(() -> comment.validateDeleteAuth(user))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void getter() {
        String email = "selfnews@gmail.com";
        String password = "password1!";
        User userByPost = User.of(email, password, true);

        String title = "title";
        String content = "content";
        String link = "link";
        Category category = new Category("java");

        Post post = Post.of(title, content, link, userByPost, category);

        String commentContent = "좋은 게시물입니다!";
        Comment comment = new Comment(commentContent, userByPost, post);

        Assertions.assertThat(comment.getId()).isNull();
        Assertions.assertThat(comment.getPost()).isSameAs(post);
        Assertions.assertThat(comment.getUser()).isSameAs(userByPost);
        Assertions.assertThat(comment.getContent()).isEqualTo(commentContent);
        Assertions.assertThat(comment.getCreatedAt()).isBefore(LocalDateTime.now());
    }
}