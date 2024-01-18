package com.douunderstandapi.post.dto.response;

import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.user.domain.User;
import lombok.Builder;

@Builder
public record PostDeleteResponse(
        Long id,
        String content,
        Long likeCount,
        Long commentCount,
        String createdAt,
        Long userId,
        String userEmail,
        Boolean likeMe
) {
    public static PostDeleteResponse of(Post post, User user) {
        return PostDeleteResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .likeCount(0L)
                .commentCount(0L)
                .createdAt(post.getCreatedAt().toString())
                .userId(user.getId())
                .userEmail(user.getEmail())
                .likeMe(false)
                .build();
    }
}
