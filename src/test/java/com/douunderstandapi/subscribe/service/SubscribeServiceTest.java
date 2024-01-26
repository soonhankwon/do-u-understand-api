package com.douunderstandapi.subscribe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.douunderstandapi.category.domain.Category;
import com.douunderstandapi.comment.repository.CommentRepository;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.post.repository.PostRepository;
import com.douunderstandapi.subscribe.domain.Subscribe;
import com.douunderstandapi.subscribe.dto.response.SubscribeAddResponse;
import com.douunderstandapi.subscribe.dto.response.SubscribeCancelResponse;
import com.douunderstandapi.subscribe.dto.response.SubscribePostsGetResponse;
import com.douunderstandapi.subscribe.repository.SubscribeRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class SubscribeServiceTest {

    @InjectMocks
    private SubscribeService subscribeService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscribeRepository subscribeRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Test
    void getSubscribePosts() {
        String email = "selfnews@gmail.com";
        String categoryName1 = "java";
        String categoryName2 = "jpa";

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(createUser()));
        when(subscribeRepository.findAllByUser(any(User.class), any(Pageable.class)))
                .thenReturn(createSubscribePage(List.of(categoryName1, categoryName2)));
        when(commentRepository.countAllByPost(any(Post.class)))
                .thenReturn(1L);

        SubscribePostsGetResponse responseQueryNull = subscribeService.getSubscribePosts(email, 0, null);
        SubscribePostsGetResponse responseQueryEmpty = subscribeService.getSubscribePosts(email, 0, "");
        SubscribePostsGetResponse responseQueryInclude = subscribeService.getSubscribePosts(email, 0, "java");

        assertThat(responseQueryNull.postList().size()).isEqualTo(2);
        assertThat(responseQueryEmpty.postList().size()).isEqualTo(2);
        assertThat(responseQueryInclude.postList().size()).isEqualTo(1);
        assertThat(responseQueryInclude.postList().getFirst().categoryName()).isEqualTo("java");
    }

    private User createUser() {
        return User.of("selfnews@gmail.com", "password1!", true);
    }

    private Page<Subscribe> createSubscribePage(List<String> categoryNames) {
        List<Subscribe> subscribes = categoryNames.stream()
                .map(name -> new Subscribe(createUser(), createPost(name)))
                .collect(Collectors.toList());

        return new PageImpl<>(subscribes);
    }

    private Post createPost(String categoryName) {
        return Post.of("함수 네이밍 룰 컨벤션",
                "GET 요청을 처리하는 메서드의 네이밍 규칭......",
                "https://sdnksnd/sds123",
                createUser(),
                new Category(categoryName));
    }

    @Test
    void addSubscribe() {
        String email = "selfnews@gmail.com";
        String categoryName1 = "java";
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(createUser()));
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.of(createPost(categoryName1)));
        when(subscribeRepository.findByUserAndPost(any(User.class), any(Post.class)))
                .thenReturn(Optional.empty());
        when(subscribeRepository.save(any(Subscribe.class)))
                .thenReturn(any(Subscribe.class));

        SubscribeAddResponse response = subscribeService.addSubscribe(email, 1L);

        assertThat(response.isAdded()).isTrue();
    }

    @Test
    void addSubscribe_duplicated_exception() {
        String email = "selfnews@gmail.com";
        String categoryName1 = "java";
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(createUser()));
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.of(createPost(categoryName1)));
        when(subscribeRepository.findByUserAndPost(any(User.class), any(Post.class)))
                .thenReturn(Optional.of(createSubscribe(categoryName1)));

        assertThatThrownBy(() -> subscribeService.addSubscribe(email, 1L))
                .isInstanceOf(CustomException.class);
    }

    private Subscribe createSubscribe(String categoryName) {
        return new Subscribe(createUser(), createPost(categoryName));
    }

    @Test
    void cancelSubscribe() {
        String email = "selfnews@gmail.com";
        String categoryName1 = "java";
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(createUser()));
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.of(createPost(categoryName1)));
        doNothing().when(subscribeRepository).deleteByPostAndUser(any(Post.class), any(User.class));

        SubscribeCancelResponse response = subscribeService.cancelSubscribe(email, 1L);

        Assertions.assertThat(response.isCanceled()).isTrue();
    }
}