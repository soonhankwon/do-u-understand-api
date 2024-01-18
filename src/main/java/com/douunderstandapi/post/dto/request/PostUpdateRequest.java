package com.douunderstandapi.post.dto.request;

public record PostUpdateRequest(
        String title,
        String content,
        String link
) {
}
