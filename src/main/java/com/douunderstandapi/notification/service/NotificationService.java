package com.douunderstandapi.notification.service;

import com.douunderstandapi.common.utils.mail.EmailUtils;
import com.douunderstandapi.common.utils.mail.dto.NotificationEmailDTO;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.subscribe.domain.Subscribe;
import com.douunderstandapi.subscribe.repository.SubscribeRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class NotificationService {

    private final SubscribeRepository subscribeRepository;
    private final UserRepository userRepository;
    private final EmailUtils emailUtils;

    @Scheduled(cron = "0 0 8 * * *")
    public void sendUnderstandNotificationInMorning() {
        List<User> users = findUserByAllowedNotification();
        sendPriorityPostsByEmail(users);
    }

    @Scheduled(cron = "0 0 13 * * *")
    public void sendUnderstandNotificationInAfternoon() {
        List<User> users = findUserByAllowedNotification();
        sendPriorityPostsByEmail(users);
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void sendUnderstandNotificationInEvening() {
        List<User> users = findUserByAllowedNotification();
        sendPriorityPostsByEmail(users);
    }

    private List<User> findUserByAllowedNotification() {
        return userRepository.findAllByIsAllowedNotification(true);
    }

    private void sendPriorityPostsByEmail(List<User> users) {

        users.forEach(u -> {
            List<Subscribe> subscribes = subscribeRepository.findAllByUser(u);

            // 알람 신청한 지식중 알람 카운터가 가장 적은것을 하나 전송한다(Round Robin)
            subscribes.stream()
                    .map(Subscribe::getPost)
                    .min(Comparator.comparing(Post::getNotificationCount))
                    .ifPresent(k -> {
                        k.increaseNotificationCount();
                        sendEmail(u, k);
                    });
        });
    }

    private void sendEmail(User user, Post post) {
        emailUtils.sendPostNotificationMessage(user.getEmail(), NotificationEmailDTO.from(post));
    }
}
