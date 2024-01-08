package com.douunderstandapi.common.utils.mail.dto;

import com.douunderstandapi.knowledge.domain.Knowledge;

public record NotificationEmailDTO(
        String title,
        String content,
        String link
) {
    public static NotificationEmailDTO from(Knowledge knowledge) {
        return new NotificationEmailDTO(
                knowledge.getTitle(),
                knowledge.getContent(),
                knowledge.getLink());
    }
}
