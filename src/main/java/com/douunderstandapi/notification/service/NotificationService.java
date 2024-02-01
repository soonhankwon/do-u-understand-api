package com.douunderstandapi.notification.service;

import com.douunderstandapi.common.utils.discord.DiscordUtils;
import com.douunderstandapi.common.utils.discord.dto.DiscordWebhookRequest;
import com.douunderstandapi.common.utils.mail.EmailUtils;
import com.douunderstandapi.common.utils.mail.dto.NotificationEmailDTO;
import com.douunderstandapi.notification.event.NotificationFailEvent;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.post.repository.PostRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class NotificationService {

    @Value("${discord.webhook.server-url}")
    private String discordServerUrl;

    private final UserRepository userRepository;
    private final EmailUtils emailUtils;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final DiscordUtils discordUtils;

    @Scheduled(cron = "0 0 8 * * *")
    public void sendUnderstandNotificationInMorning() {
        List<User> users = findUserByAllowedNotificationAndExistsSubscribe();
        sendPriorityPostsByEmail(users);
    }

    @Scheduled(cron = "0 0 13 * * *")
    public void sendUnderstandNotificationInAfternoon() {
        List<User> users = findUserByAllowedNotificationAndExistsSubscribe();
        sendPriorityPostsByEmail(users);
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void sendUnderstandNotificationInEvening() {
        List<User> users = findUserByAllowedNotificationAndExistsSubscribe();
        sendPriorityPostsByEmail(users);
    }

    private List<User> findUserByAllowedNotificationAndExistsSubscribe() {
        return userRepository.findAllByIsAllowedNotificationAndExistsSubscribe();
    }

    private void sendPriorityPostsByEmail(List<User> users) {
        Map<String, Boolean> map = users.parallelStream()
                .collect(Collectors.toMap(User::getEmail, isNotified -> false));

        AtomicInteger successNotificationCountRef = new AtomicInteger();
        users.forEach(user -> {
            // 알람 신청한 지식중 알람 카운터가 가장 적은것을 하나 전송한다(Round Robin)
            Page<Post> postPage = postRepository.findPostWithMinNotificationCount(user,
                    PageRequest.of(0, 1));
            List<Post> posts = postPage.getContent();
            String email = user.getEmail();

            assert !posts.isEmpty();

            try {
                Post minNotificationCountPost = posts.getFirst();
                minNotificationCountPost.increaseNotificationCount();
                sendEmail(user, minNotificationCountPost);
            } catch (Exception ex) {
                log.warn(
                        String.format(
                                "cause={%s} msg={%s}",
                                ex.getCause(), ex.getMessage()
                        )
                );
                return;
            }
            map.put(email, true);
            successNotificationCountRef.getAndIncrement();
        });

        AtomicInteger failNotificationCountRef = new AtomicInteger();
        // 맵에 false로 남아있다면 failEvent
        map.keySet().forEach(failUserEmail -> {
            Boolean isNotified = map.get(failUserEmail);
            if (!isNotified) {
                failNotificationCountRef.getAndIncrement();
                eventPublisher.publishEvent(new NotificationFailEvent(failUserEmail));
            }
        });
        map.clear();

        // 이메일 알람 결과 리포트 웹훅 전송 로직
        int successNotificationCount = successNotificationCountRef.get();
        int failNotificationCount = failNotificationCountRef.get();
        String reportContent = getEmailNotificationResultReport(successNotificationCount, failNotificationCount);

        discordUtils.sendDiscordWebhook(DiscordWebhookRequest.of(reportContent, discordServerUrl));
    }

    private void sendEmail(User user, Post post) {
        emailUtils.sendPostNotificationMessage(user.getEmail(), NotificationEmailDTO.from(post));
    }

    private String getEmailNotificationResultReport(int successCount, int failCount) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");

        return "[" + LocalDateTime.now().format(formatter) + "]" +
                " 이메일 알람 성공: " + successCount +
                " 이메일 알람 실패: " + failCount;
    }
}
