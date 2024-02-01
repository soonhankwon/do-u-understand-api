package com.douunderstandapi.common.utils.discord;

import com.douunderstandapi.common.utils.discord.dto.DiscordWebhookRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class DiscordUtils {

    @Value("${discord.webhook.baseurl}")
    private String baseurl;

    public void sendDiscordWebhook(DiscordWebhookRequest request) {
        WebClient.create(baseurl)
                .post()
                .uri(request.targetDiscordUrl())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
    }
}
