package com.douunderstandapi.post.dto.response;

import com.douunderstandapi.category.domain.Category;
import com.douunderstandapi.post.domain.Post;
import lombok.Builder;

@Builder
public record PostUpdateResponse(
        Long id,
        String title,
        String content,
        String link,
        Long commentCount,
        String createdAt,
        Long userId,
        String userEmail,
        String categoryName,
        Boolean subscribeMe
) {
    public static PostUpdateResponse of(Post post, Category category, boolean subscribeMe) {
        return PostUpdateResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .link(post.getLink())
                .commentCount(0L)
                .createdAt(post.getCreatedAt().toString())
                .userId(post.getUser().getId())
                .userEmail(post.getUser().getEmail())
                .categoryName(category.getName())
                .subscribeMe(subscribeMe)
                .build();
    }
}
