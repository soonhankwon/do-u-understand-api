package com.douunderstandapi.notification.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.douunderstandapi.category.domain.Category;
import com.douunderstandapi.common.utils.discord.DiscordUtils;
import com.douunderstandapi.common.utils.discord.dto.DiscordWebhookRequest;
import com.douunderstandapi.common.utils.mail.EmailUtils;
import com.douunderstandapi.common.utils.mail.dto.NotificationEmailDTO;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.post.repository.PostRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private EmailUtils emailUtils;

    @Mock
    private DiscordUtils discordUtils;

    @Spy
    @InjectMocks
    private NotificationService notificationService;

    @Test
    @DisplayName("포스트 알람 - 서비스 로직 테스트(아침)")
    void sendUnderstandNotificationInMorning() {
        User user = createUser();
        Category category = new Category("java");
        List<User> users = List.of(user);
        List<Post> posts = List.of(Post.of("title", "content", "link", user, category));

        Page<Post> postPage = new PageImpl<>(posts);

        when(userRepository.findAllByIsAllowedNotification(anyBoolean()))
                .thenReturn(users);
        when(postRepository.findPostWithMinNotificationCount(any(User.class), any(Pageable.class)))
                .thenReturn(postPage);
        when(emailUtils.sendPostNotificationMessage(any(String.class), any(NotificationEmailDTO.class)))
                .thenReturn("success");
        doNothing().when(discordUtils).sendDiscordWebhook(any(DiscordWebhookRequest.class));

        notificationService.sendUnderstandNotificationInMorning();

        verify(notificationService, times(1)).sendUnderstandNotificationInMorning();
    }

    @Test
    @DisplayName("포스트 알람 - 서비스 로직 테스트(점심)")
    void sendUnderstandNotificationInAfternoon() {
        User user = createUser();
        Category category = new Category("java");
        List<User> users = List.of(user);
        List<Post> posts = List.of(Post.of("title", "content", "link", user, category));

        Page<Post> postPage = new PageImpl<>(posts);

        when(userRepository.findAllByIsAllowedNotification(anyBoolean()))
                .thenReturn(users);
        when(postRepository.findPostWithMinNotificationCount(any(User.class), any(Pageable.class)))
                .thenReturn(postPage);
        when(emailUtils.sendPostNotificationMessage(any(String.class), any(NotificationEmailDTO.class)))
                .thenReturn("success");
        doNothing().when(discordUtils).sendDiscordWebhook(any(DiscordWebhookRequest.class));

        notificationService.sendUnderstandNotificationInAfternoon();

        verify(notificationService, times(1)).sendUnderstandNotificationInAfternoon();
    }

    @Test
    @DisplayName("포스트 알람 - 서비스 로직 테스트(저녁)")
    void sendUnderstandNotificationInEvening() {
        User user = createUser();
        Category category = new Category("java");
        List<User> users = List.of(user);
        List<Post> posts = List.of(Post.of("title", "content", "link", user, category));

        Page<Post> postPage = new PageImpl<>(posts);

        when(userRepository.findAllByIsAllowedNotification(anyBoolean()))
                .thenReturn(users);
        when(postRepository.findPostWithMinNotificationCount(any(User.class), any(Pageable.class)))
                .thenReturn(postPage);
        when(emailUtils.sendPostNotificationMessage(any(String.class), any(NotificationEmailDTO.class)))
                .thenReturn("success");
        doNothing().when(discordUtils).sendDiscordWebhook(any(DiscordWebhookRequest.class));

        notificationService.sendUnderstandNotificationInEvening();

        verify(notificationService, times(1)).sendUnderstandNotificationInEvening();
    }

    private User createUser() {
        return User.of("test@gmail.com", "password1!", true);
    }
}