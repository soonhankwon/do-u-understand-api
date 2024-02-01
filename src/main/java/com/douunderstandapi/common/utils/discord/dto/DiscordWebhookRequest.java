package com.douunderstandapi.common.utils.discord.dto;

public record DiscordWebhookRequest(
        String content,
        String targetDiscordUrl
) {
    public static DiscordWebhookRequest of(String reportContent, String discordServerUrl) {
        return new DiscordWebhookRequest(reportContent, discordServerUrl);
    }
}
