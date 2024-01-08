package com.douunderstandapi.notification.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.douunderstandapi.common.utils.mail.EmailUtils;
import com.douunderstandapi.common.utils.mail.dto.NotificationEmailDTO;
import com.douunderstandapi.knowledge.domain.Knowledge;
import com.douunderstandapi.knowledge.repository.KnowledgeRepository;
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
    private KnowledgeRepository knowledgeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailUtils emailUtils;

    @Spy
    @InjectMocks
    private NotificationService notificationService;

    @Test
    @DisplayName("지식 알람 - 서비스 로직 테스트(아침)")
    void sendUnderstandNotificationInMorning() {
        List<User> users = List.of(createUser());
        List<Knowledge> knowledges = List.of(createKnowledge());

        when(userRepository.findAllByIsAllowedNotification(anyBoolean()))
                .thenReturn(users);
        when(knowledgeRepository.findAllByUserAndIsSubscribe(any(User.class), anyBoolean()))
                .thenReturn(knowledges);
        when(emailUtils.sendKnowledgeNotificationMessage(anyString(), any(NotificationEmailDTO.class)))
                .thenReturn("success");

        notificationService.sendUnderstandNotificationInMorning();

        verify(notificationService, times(1)).sendUnderstandNotificationInMorning();
    }

    @Test
    @DisplayName("지식 알람 - 서비스 로직 테스트(점심)")
    void sendUnderstandNotificationInAfternoon() {
        List<User> users = List.of(createUser());
        List<Knowledge> knowledges = List.of(createKnowledge());

        when(userRepository.findAllByIsAllowedNotification(anyBoolean()))
                .thenReturn(users);
        when(knowledgeRepository.findAllByUserAndIsSubscribe(any(User.class), anyBoolean()))
                .thenReturn(knowledges);
        when(emailUtils.sendKnowledgeNotificationMessage(anyString(), any(NotificationEmailDTO.class)))
                .thenReturn("success");

        notificationService.sendUnderstandNotificationInEvening();

        verify(notificationService, times(1)).sendUnderstandNotificationInEvening();
    }

    @Test
    @DisplayName("지식 알람 - 서비스 로직 테스트(저녁)")
    void sendUnderstandNotificationInEvening() {
        List<User> users = List.of(createUser());
        List<Knowledge> knowledges = List.of(createKnowledge());

        when(userRepository.findAllByIsAllowedNotification(anyBoolean()))
                .thenReturn(users);
        when(knowledgeRepository.findAllByUserAndIsSubscribe(any(User.class), anyBoolean()))
                .thenReturn(knowledges);
        when(emailUtils.sendKnowledgeNotificationMessage(anyString(), any(NotificationEmailDTO.class)))
                .thenReturn("success");

        notificationService.sendUnderstandNotificationInAfternoon();

        verify(notificationService, times(1)).sendUnderstandNotificationInAfternoon();
    }

    private Knowledge createKnowledge() {
        return Knowledge.of("함수 네이밍 룰 컨벤션",
                "GET 요청을 처리하는 메서드의 네이밍 규칭......",
                "https://sdnksnd/sds123", createUser());
    }

    private User createUser() {
        return User.of("test@gmail.com", "password1!", true);
    }
}