package com.douunderstandapi.comment.dto;

import com.douunderstandapi.comment.domain.Comment;

public record CommentDTO(
        Long id,
        Long postId,
        String content,
        String createdAt,
        Long userId,
        String userEmail
) {
    public static CommentDTO of(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getPost().getId(),
                comment.getContent(),
                comment.getCreatedAt().toString(),
                comment.getUser().getId(),
                comment.getUser().getEmail()
        );
    }
}
