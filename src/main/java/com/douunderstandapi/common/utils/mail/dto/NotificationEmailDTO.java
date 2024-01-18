package com.douunderstandapi.common.utils.mail.dto;

import com.douunderstandapi.post.domain.Post;

public record NotificationEmailDTO(
        String title,
        String content,
        String link
) {
    public static NotificationEmailDTO from(Post post) {
        return new NotificationEmailDTO(
                post.getTitle(),
                post.getContent(),
                post.getLink());
    }
}
