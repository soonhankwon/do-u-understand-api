package com.douunderstandapi.post.dto.response;

import com.douunderstandapi.post.domain.Post;
import lombok.Builder;

@Builder
public record PostGetResponse(
        Long id,
        Long userId,
        String userEmail,
        String content,
        String createdAt,
        Long commentCount,
        Long likeCount
) {
    public static PostGetResponse from(Post post) {
        return new PostGetResponse(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getEmail(),
                post.getContent(),
                post.getCreatedAt().toString(),
                1L,
                1L
        );
    }
}
