package com.douunderstandapi.post.dto;

import com.douunderstandapi.post.domain.Post;
import lombok.Builder;

@Builder
public record PostDTO(
        Long id,
        String title,
        String content,
        String link,
        Long commentCount,
        String userEmail,
        Long userId,
        String createdAt,
        Boolean subscribeMe,
        String categoryName
) {

    public static PostDTO of(Post post, Long commentCount, Boolean subscribeMe) {
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .link(post.getLink())
                .commentCount(commentCount)
                .userEmail(post.getUser().getEmail())
                .userId(post.getUser().getId())
                .createdAt(post.getCreatedAt().toString())
                .subscribeMe(subscribeMe)
                .categoryName(post.getCategory().getName())
                .build();
    }
}
