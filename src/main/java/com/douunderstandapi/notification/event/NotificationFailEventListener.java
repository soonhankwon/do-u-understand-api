package com.douunderstandapi.notification.event;

import com.douunderstandapi.common.utils.discord.DiscordUtils;
import com.douunderstandapi.common.utils.discord.dto.DiscordWebhookRequest;
import com.douunderstandapi.common.utils.mail.EmailUtils;
import com.douunderstandapi.common.utils.mail.dto.NotificationEmailDTO;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.post.repository.PostRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationFailEventListener {

    @Value("${discord.webhook.server-url}")
    private String discordServerUrl;

    private final PostRepository postRepository;
    private final EmailUtils emailUtils;
    private final DiscordUtils discordUtils;
    private final Set<NotificationFailEvent> retrySet = new HashSet<>();

    @Async
    @EventListener
    public void handleNotificationFailEvent(NotificationFailEvent notificationFailEvent) {
        retrySet.add(notificationFailEvent);
    }

    @Scheduled(cron = "0 30 08 * * *")
    public void retryNotificationInMorning() {
        retryNotification();
    }

    @Scheduled(cron = "0 30 13 * * *")
    public void retryNotificationInAfternoon() {
        retryNotification();
    }

    @Scheduled(cron = "0 30 20 * * *")
    public void retryNotificationInEvening() {
        retryNotification();
    }

    public void retryNotification() {
        if (this.retrySet.isEmpty()) {
            return;
        }
        try {
            retrySet.forEach(failEvent -> {
                String email = failEvent.email();
                Page<Post> postPage = postRepository.findPostWithMinNotificationCount(email,
                        PageRequest.of(0, 1));
                List<Post> posts = postPage.getContent();
                if (posts.isEmpty()) {
                    return;
                }
                Post minCountPost = posts.getFirst();
                minCountPost.increaseNotificationCount();
                sendEmail(email, minCountPost);
                retrySet.remove(failEvent);
            });
        } catch (Exception ex) {
            log.warn(
                    String.format(
                            "cause={%s} msg={%s}",
                            ex.getCause(), ex.getMessage()
                    )
            );
        }

        StringBuilder sb = new StringBuilder();
        retrySet.forEach(i -> sb.append(i.email()).append("\n"));
        String failEmails = sb.toString();
        String reportContent = getEmailRetryNotificationResultReport(retrySet.size(),
                failEmails);
        retrySet.clear();

        discordUtils.sendDiscordWebhook(DiscordWebhookRequest.of(reportContent, discordServerUrl));
    }

    private void sendEmail(String email, Post post) {
        emailUtils.sendPostNotificationMessage(email, NotificationEmailDTO.from(post));
    }

    private String getEmailRetryNotificationResultReport(int failCount, String failEmails) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");

        return "[" + LocalDateTime.now().format(formatter) + "]" +
                " 이메일 알람 실패: " + failCount + " 실패 이메일 목록: " + failEmails;
    }
}
