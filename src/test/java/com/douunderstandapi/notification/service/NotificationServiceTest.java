package com.douunderstandapi.notification.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.douunderstandapi.common.utils.mail.EmailUtils;
import com.douunderstandapi.subscribe.domain.Subscribe;
import com.douunderstandapi.subscribe.repository.SubscribeRepository;
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

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private SubscribeRepository subscribeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailUtils emailUtils;

    @Spy
    @InjectMocks
    private NotificationService notificationService;

    @Test
    @DisplayName("포스트 알람 - 서비스 로직 테스트(아침)")
    void sendUnderstandNotificationInMorning() {
        List<User> users = List.of(createUser());
        List<Subscribe> subscribes = List.of();

        when(userRepository.findAllByIsAllowedNotification(anyBoolean()))
                .thenReturn(users);
        when(subscribeRepository.findAllByUser(any(User.class)))
                .thenReturn(subscribes);
//        when(emailUtils.sendPostNotificationMessage(anyString(), any(NotificationEmailDTO.class)))
//                .thenReturn("success");

        notificationService.sendUnderstandNotificationInMorning();

        verify(notificationService, times(1)).sendUnderstandNotificationInMorning();
    }

    @Test
    @DisplayName("포스트 알람 - 서비스 로직 테스트(점심)")
    void sendUnderstandNotificationInAfternoon() {
        List<User> users = List.of(createUser());
        List<Subscribe> subscribes = List.of();

        when(userRepository.findAllByIsAllowedNotification(anyBoolean()))
                .thenReturn(users);
        when(subscribeRepository.findAllByUser(any(User.class)))
                .thenReturn(subscribes);
//        when(emailUtils.sendPostNotificationMessage(anyString(), any(NotificationEmailDTO.class)))
//                .thenReturn("success");

        notificationService.sendUnderstandNotificationInEvening();

        verify(notificationService, times(1)).sendUnderstandNotificationInEvening();
    }

    @Test
    @DisplayName("포스트 알람 - 서비스 로직 테스트(저녁)")
    void sendUnderstandNotificationInEvening() {
        List<User> users = List.of(createUser());
        List<Subscribe> subscribes = List.of();

        when(userRepository.findAllByIsAllowedNotification(anyBoolean()))
                .thenReturn(users);
        when(subscribeRepository.findAllByUser(any(User.class)))
                .thenReturn(subscribes);
//        when(emailUtils.sendPostNotificationMessage(anyString(), any(NotificationEmailDTO.class)))
//                .thenReturn("success");

        notificationService.sendUnderstandNotificationInAfternoon();

        verify(notificationService, times(1)).sendUnderstandNotificationInAfternoon();
    }

    private User createUser() {
        return User.of("test@gmail.com", "password1!", true);
    }
}