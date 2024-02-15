package com.douunderstandapi.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.douunderstandapi.category.domain.Category;
import com.douunderstandapi.category.repository.CategoryRepository;
import com.douunderstandapi.comment.repository.CommentRepository;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.post.dto.request.PostAddRequest;
import com.douunderstandapi.post.dto.request.PostUpdateRequest;
import com.douunderstandapi.post.dto.response.PostAddResponse;
import com.douunderstandapi.post.dto.response.PostGetResponse;
import com.douunderstandapi.post.dto.response.PostUpdateResponse;
import com.douunderstandapi.post.repository.PostRepository;
import com.douunderstandapi.subscribe.domain.Subscribe;
import com.douunderstandapi.subscribe.repository.SubscribeRepository;
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
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private SubscribeRepository subscribeRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("포스트 생성 - 카테고리가 존재할 경우")
    void addPost() {
        String categoryName = "programming";
        String email = "test@gmail.com";
        User user = createUser(email);
        when(categoryRepository.findByName(any(String.class)))
                .thenReturn(Optional.of(createCategory(categoryName)));
        when(postRepository.save(any(Post.class)))
                .thenReturn(createPost(user));
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(user));

        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String link = "https://sdnksnd/sds123";

        PostAddRequest request = createPostAddRequest(title, content, link, categoryName);

        PostAddResponse response = postService.addPost(email, request);

        assertThat(response.content()).isEqualTo(content);
    }

    @Test
    @DisplayName("포스트 생성 - 카테고리가 없을 경우")
    void addPost_category_not_exists() {
        String categoryName = "programming";
        String email = "test@gmail.com";
        User user = createUser(email);
        when(categoryRepository.findByName(any(String.class)))
                .thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(createCategory(categoryName));
        when(postRepository.save(any(Post.class)))
                .thenReturn(createPost(user));
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(user));

        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String link = "https://sdnksnd/sds123";

        PostAddRequest request = createPostAddRequest(title, content, link, categoryName);

        PostAddResponse response = postService.addPost(email, request);

        assertThat(response.categoryName()).isEqualTo(categoryName);
    }

    @Test
    @DisplayName("포스트 생성 - 유저 email 미존재 예외처리")
    void addPost_user_email_not_exist_exception() {
        String categoryName = "programming";
        String email = "test@gmail.com";
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        String title = "함수 네이밍 룰 컨벤션";
        String content = "GET 요청을 처리하는 메서드의 네이밍 규칭......";
        String link = "https://sdnksnd/sds123";

        PostAddRequest request = createPostAddRequest(title, content, link, categoryName);

        assertThatThrownBy(() ->
                postService.addPost(email, request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("포스트 상세조회(단건) - 정상호출")
    void findPost() {
        String email = "test@gmail.com";
        User user = createUser(email);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(createPost(user)));
        when(commentRepository.countAllByPost(any(Post.class))).thenReturn(10L);

        PostGetResponse response = postService.findPost(user.getEmail(), 1L);

        assertThat(response.content()).isEqualTo("GET 요청을 처리하는 메서드의 네이밍 규칭......");
        assertThat(response.commentCount()).isEqualTo(10L);
    }

    @Test
    @DisplayName("포스트 상세조회(단건) - 자신의 지식이 아닌것을 조회하는 경우 Exception")
    void findPost_not_my_post_exception() {
        String email1 = "test1@gmail.com";
        User user = createUser(email1);
        String email2 = "test2@gmail.com";
        User anotherUser = createUser(email2);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        // post의 작성자가 anothorUser: 자신의 post가 아님
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(createPost(anotherUser)));

        assertThatThrownBy(() -> postService.findPost(anotherUser.getEmail(), 1L))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("포스트 업데이트 - 카테고리가 있는 경우")
    void updatePost() {
        String email = "test@gmail.com";
        User user = createUser(email);
        String categoryName = "programming";
        Post post = createPost(user);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(createCategory(categoryName)));
        when(subscribeRepository.findByUserAndPost(any(User.class), any(Post.class)))
                .thenReturn(Optional.of(createSubscribe(user, post)));

        PostUpdateRequest request = new PostUpdateRequest("RESTful API 수정본", "수정된 버전의 내용",
                "https://mock-link", "java");

        PostUpdateResponse response = postService.update(user.getEmail(), 1L, request);

        assertThat(response.content()).isEqualTo(request.content());
    }

    @Test
    @DisplayName("포스트 업데이트 - 카테고리가 없는 경우")
    void updatePost_category_not_exists() {
        String email = "test@gmail.com";
        User user = createUser(email);
        String categoryName = "java";
        Post post = createPost(user);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(subscribeRepository.findByUserAndPost(any(User.class), any(Post.class)))
                .thenReturn(Optional.of(createSubscribe(user, post)));

        PostUpdateRequest request = new PostUpdateRequest("RESTful API 수정본", "수정된 버전의 내용",
                "https://mock-link", categoryName);

        PostUpdateResponse response = postService.update(user.getEmail(), 1L, request);

        assertThat(response.categoryName()).isEqualTo(categoryName);
    }

    @Test
    @DisplayName("포스트 업데이트 - 구독이 없는 경우")
    void updatePost_subscribe_not_exists() {
        String email = "test@gmail.com";
        User user = createUser(email);
        String categoryName = "java";
        Post post = createPost(user);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(subscribeRepository.findByUserAndPost(any(User.class), any(Post.class)))
                .thenReturn(Optional.empty());

        PostUpdateRequest request = new PostUpdateRequest("RESTful API 수정본", "수정된 버전의 내용",
                "https://mock-link", categoryName);

        PostUpdateResponse response = postService.update(user.getEmail(), 1L, request);

        assertThat(response.subscribeMe()).isFalse();
    }

    @Test
    @DisplayName("포스트 업데이트 - 자신의 지식이 아닌것을 업데이트하는 경우 Exception")
    void updatePost_not_my_post_exception() {
        String email1 = "test1@gmail.com";
        User user = createUser(email1);
        String email2 = "test2@gmail.com";
        User anotherUser = createUser(email2);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(createPost(anotherUser)));

        PostUpdateRequest request = new PostUpdateRequest("RESTful API 수정본", "수정된 버전의 내용",
                "https://mock-link", "java");

        assertThatThrownBy(() -> postService.update(user.getEmail(), 1L, request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("포스트 삭제 - 정상호출")
    void deletePost() {
        String email = "test@gmail.com";
        User user = createUser(email);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(createPost(user)));

        Boolean response = postService.delete(user.getEmail(), 1L);

        assertThat(response).isTrue();
    }

    @Test
    @DisplayName("포스트 삭제 - 자신의 지식이 아닌것을 삭제하는 경우 Exception")
    void deletePost_not_my_post_exception() {
        String email1 = "test1@gmail.com";
        User user = createUser(email1);
        String email2 = "test2@gmail.com";
        User anotherUser = createUser(email2);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(createPost(anotherUser)));

        assertThatThrownBy(() -> postService.delete(user.getEmail(), 1L))
                .isInstanceOf(CustomException.class);
    }

    private PostAddRequest createPostAddRequest(String title, String content, String link, String categoryName) {
        return new PostAddRequest(title, content, link, categoryName);
    }

    private Post createPost(User user) {
        return Post.of("함수 네이밍 룰 컨벤션",
                "GET 요청을 처리하는 메서드의 네이밍 규칭......",
                "https://sdnksnd/sds123", user,
                new Category("java"));
    }

    private User createUser(String email) {
        return User.of(email, "password1!", true);
    }

    private Category createCategory(String categoryName) {
        return new Category(categoryName);
    }

    private Subscribe createSubscribe(User user, Post post) {
        return new Subscribe(user, post);
    }
}