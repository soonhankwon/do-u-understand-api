package com.douunderstandapi.post.dto;

import com.douunderstandapi.post.domain.Post;
import lombok.Builder;

@Builder
public record PostDTO(
        Long id,
        String title,
        String content,
        String link,
        Long likeCount,
        Long commentCount,
        Boolean isUnderstand,
        String userEmail,
        Long userId,
        String createdAt,
        Boolean likeMe
) {

    public static PostDTO from(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .link(post.getLink())
                .likeCount(0L)
                .commentCount(0L)
                .isUnderstand(post.getIsUnderstand())
                .userEmail(post.getUser().getEmail())
                .userId(post.getUser().getId())
                .createdAt(post.getCreatedAt().toString())
                .likeMe(false)
                .build();
    }
}
