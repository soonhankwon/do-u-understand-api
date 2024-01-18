package com.douunderstandapi.comment.dto.response;

import com.douunderstandapi.comment.domain.Comment;
import lombok.Builder;

@Builder
public record CommentAddResponse(
        Long id,
        Long postId,
        String content,
        String createdAt,
        Long userId,
        String userEmail
) {
    public static CommentAddResponse from(Comment comment) {
        return CommentAddResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt().toString())
                .userId(comment.getUser().getId())
                .userEmail(comment.getUser().getEmail())
                .build();
    }
}
