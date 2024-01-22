package com.douunderstandapi.post.dto.response;

import com.douunderstandapi.post.domain.Post;
import lombok.Builder;

@Builder
public record PostGetResponse(
        Long id,
        Long userId,
        String userEmail,
        String title,
        String content,
        String link,
        String createdAt,
        Long commentCount,
        String categoryName
) {
    public static PostGetResponse of(Post post, Long commentCount) {
        return new PostGetResponse(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getEmail(),
                post.getTitle(),
                post.getContent(),
                post.getLink(),
                post.getCreatedAt().toString(),
                commentCount,
                post.getCategory().getName()
        );
    }
}
