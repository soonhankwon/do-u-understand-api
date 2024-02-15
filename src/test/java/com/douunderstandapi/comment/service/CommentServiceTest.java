package com.douunderstandapi.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.douunderstandapi.category.domain.Category;
import com.douunderstandapi.comment.domain.Comment;
import com.douunderstandapi.comment.dto.request.CommentAddRequest;
import com.douunderstandapi.comment.dto.request.CommentDeleteRequest;
import com.douunderstandapi.comment.dto.response.CommentAddResponse;
import com.douunderstandapi.comment.dto.response.CommentDeleteResponse;
import com.douunderstandapi.comment.dto.response.CommentsGetResponse;
import com.douunderstandapi.comment.repository.CommentRepository;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.post.repository.PostRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("코멘트 목록 조회 - 정상호출")
    void getComments() {
        String email = "test@gmail.com";
        User user = createUser(email);
        Post post = createPost(user);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        String commentContent = "good!";
        when(commentRepository.findAllByPost(post)).thenReturn(List.of(createComment(commentContent, user, post)));

        CommentsGetResponse response = commentService.findComments(email, 1L);

        assertThat(response.comments().getFirst().userEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("코멘트 목록 조회 - 이메일이 null인 경우는 존재하지 않음")
    void getComments_assert_email_not_null() {
        String email = null;
        assertThatThrownBy(() -> commentService.findComments(email, 1L))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    @DisplayName("코멘트 목록 조회 - 내가 작성한 포스트가 아닌경우 예외처리(포스트의 코멘트는 본인만 작성 및 조회가능)")
    void getComments_not_my_post_exception() {
        String email1 = "test1@gmail.com";
        User user1 = createUser(email1);

        String email2 = "test2@gmail.com";
        User user2 = createUser(email2);
        Post post = createPost(user2);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        assertThatThrownBy(() -> commentService.findComments(email1, 1L))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("코멘트 목록 조회 - 유저이메일이 존재하지 않는 경우 예외처리")
    void getComments_user_email_not_exists_exception() {
        String email = "test1@gmail.com";
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.findComments(email, 1L))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("코멘트 목록 조회 - 포스트가 존재하지 않는 경우 예외처리")
    void getComments_post_not_exists_exception() {
        String email = "test1@gmail.com";
        User user = createUser(email);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> commentService.findComments(email, 1L))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("코멘트 추가 - 정상호출")
    void addComment() {
        String email = "test@gmail.com";
        User user = createUser(email);
        Post post = createPost(user);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        CommentAddRequest request = new CommentAddRequest(1L, "Good!");

        CommentAddResponse response = commentService.addComment(email, request);

        assertThat(response.content()).isEqualTo("Good!");
    }

    @Test
    @DisplayName("코멘트 추가 - postId가 null인 경우는 존재하지 않음")
    void addComment_assert_post_id_not_null() {
        String email = "test@gmail.com";
        User user = createUser(email);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        CommentAddRequest request = new CommentAddRequest(null, "Good!");

        assertThatThrownBy(() -> commentService.addComment(email, request))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    @DisplayName("코멘트 추가 - 유저이메일이 존재하지 않는 경우 예외처리")
    void addComments_user_email_not_exists_exception() {
        String email = "test1@gmail.com";
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        CommentAddRequest request = new CommentAddRequest(1L, "Good!");

        assertThatThrownBy(() -> commentService.addComment(email, request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("코멘트 추가 - 포스트가 존재하지 않는 경우 예외처리")
    void addComments_post_not_exists_exception() {
        String email = "test1@gmail.com";
        User user = createUser(email);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        CommentAddRequest request = new CommentAddRequest(1L, "Good!");

        assertThatThrownBy(() -> commentService.addComment(email, request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("코멘트 추가 - 내가 작성한 포스트가 아닌경우 예외처리(포스트의 코멘트는 본인만 작성 가능)")
    void addComment_not_my_post_exception() {
        String email1 = "test1@gmail.com";
        User user1 = createUser(email1);

        String email2 = "test2@gmail.com";
        User user2 = createUser(email2);
        Post post = createPost(user2);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        CommentAddRequest request = new CommentAddRequest(1L, "Good!");

        assertThatThrownBy(() -> commentService.addComment(email1, request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("코멘트 삭제 - 정상호출")
    void deleteComment() {
        String email = "test@gmail.com";
        User user = createUser(email);
        Post post = createPost(user);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(createComment("Good!", user, post)));

        CommentDeleteRequest request = new CommentDeleteRequest(1L);

        CommentDeleteResponse response = commentService.deleteComment(email, request);
        // 실제 DB를 사용하지 않음으로 pk: null
        assertThat(response.commentId()).isNull();
    }

    @Test
    @DisplayName("코멘트 추가 - commentId가 null인 경우는 존재하지 않음")
    void addComment_assert_comment_id_not_null() {
        String email = "test@gmail.com";
        User user = createUser(email);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        CommentDeleteRequest request = new CommentDeleteRequest(null);

        assertThatThrownBy(() -> commentService.deleteComment(email, request))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    @DisplayName("코멘트 삭제 - 유저의 코멘트가 아닌경우 예외처리")
    void deleteComment_not_my_comment_exception() {
        String email1 = "test1@gmail.com";
        User user1 = createUser(email1);
        Post post = createPost(user1);

        String email2 = "test2@gmail.com";
        User user2 = createUser(email2);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user2));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(createComment("Good!", user1, post)));

        CommentDeleteRequest request = new CommentDeleteRequest(1L);

        assertThatThrownBy(() -> commentService.deleteComment(email2, request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("코멘트 삭제 - 유저이메일이 존재하지 않는 경우 예외처리")
    void deleteComments_user_email_not_exists_exception() {
        String email = "test1@gmail.com";
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        CommentDeleteRequest request = new CommentDeleteRequest(1L);

        assertThatThrownBy(() -> commentService.deleteComment(email, request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("코멘트 삭제 - 코멘트가 존재하지 않는 경우 예외처리")
    void deleteComments_comment_not_exists_exception() {
        String email = "test1@gmail.com";
        User user = createUser(email);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        CommentDeleteRequest request = new CommentDeleteRequest(1L);

        assertThatThrownBy(() -> commentService.deleteComment(email, request))
                .isInstanceOf(CustomException.class);
    }

    private User createUser(String email) {
        return User.of(email, "password1!", true);
    }

    private Post createPost(User user) {
        return Post.of("함수 네이밍 룰 컨벤션",
                "GET 요청을 처리하는 메서드의 네이밍 규칭......",
                "https://sdnksnd/sds123", user,
                new Category("java"));
    }

    private Comment createComment(String content, User user, Post post) {
        return new Comment(content, user, post);
    }
}