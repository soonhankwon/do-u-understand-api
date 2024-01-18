package com.douunderstandapi.comment.dto.request;

public record CommentDeleteRequest(
        Long commentId,
        Long articleId
) {
}
