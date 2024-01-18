package com.douunderstandapi.post.dto.response;

import com.douunderstandapi.post.domain.Post;
import lombok.Builder;

@Builder
public record PostUpdateResponse(
        Long id,
        String title,
        String content,
        String link,
        Boolean isUnderstand,
        Long likeCount,
        Long commentCount,
        String createdAt,
        Long userId,
        String userEmail,
        Boolean likeMe
) {
    public static PostUpdateResponse from(Post post) {
        return PostUpdateResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .link(post.getLink())
                .isUnderstand(post.getIsUnderstand())
                .likeCount(0L)
                .commentCount(0L)
                .createdAt(post.getCreatedAt().toString())
                .userId(post.getUser().getId())
                .userEmail(post.getUser().getEmail())
                .likeMe(false)
                .build();
    }
}
