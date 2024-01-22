package com.douunderstandapi.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.douunderstandapi.category.domain.Category;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.post.dto.request.PostAddRequest;
import com.douunderstandapi.post.dto.request.PostUpdateRequest;
import com.douunderstandapi.post.dto.response.PostAddResponse;
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
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("포스트 생성 - 서비스 로직 테스트")
    void addPost() {
        when(postRepository.save(any(Post.class))).thenReturn(createKnowledge());
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(createUser()));
        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String email = "test@gmail.com";
        String link = "https://sdnksnd/sds123";
        String categoryName = "programming";

        PostAddRequest request = createPostAddRequest(title, content, link, categoryName);

        PostAddResponse response = postService.addPost(email, request);

        assertThat(response.content()).isEqualTo(content);
    }

    @Test
    @DisplayName("포스트 생성 - 유저 email 미존재 예외처리")
    void addPost_user_email_not_exist_exception() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String email = "test@gmail.com";
        String link = "https://sdnksnd/sds123";
        String categoryName = "programming";

        PostAddRequest request = createPostAddRequest(title, content, link, categoryName);

        assertThatThrownBy(() ->
                postService.addPost(email, request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("포스트 상세조회(단건) - 자신의 지식이 아닌것을 조회하는 경우 Exception")
    void findPost_not_my_post() {
        String email = "test@gmail.com";
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(createKnowledge()));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(createUser()));

        assertThatThrownBy(() -> postService.findPost(email, 1L))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("포스트 업데이트 - 자신의 지식이 아닌것을 업데이트하는 경우 Exception")
    void updatePost() {
        String email = "test@gmail.com";
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(createKnowledge()));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(createUser()));

        PostUpdateRequest request = new PostUpdateRequest("RESTful API 수정본", "수정된 버전의 내용",
                "https://mock-link", "java");

        assertThatThrownBy(() -> postService.update(email, 1L, request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("포스트 삭제 - 자신의 지식이 아닌것을 삭제하는 경우 Exception")
    void deletePost() {
        String email = "test@gmail.com";
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(createKnowledge()));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(createUser()));

        assertThatThrownBy(() -> postService.delete(email, 1L))
                .isInstanceOf(CustomException.class);
    }

    private PostAddRequest createPostAddRequest(String title, String content, String link, String categoryName) {
        return new PostAddRequest(title, content, link, List.of(categoryName));
    }

    private Post createKnowledge() {
        return Post.of("함수 네이밍 룰 컨벤션",
                "GET 요청을 처리하는 메서드의 네이밍 규칭......",
                "https://sdnksnd/sds123", createUser(),
                new Category("java"));
    }

    private User createUser() {
        return User.of("test@gmail.com", "password1!", true);
    }
}