package com.douunderstandapi.comment.dto.response;

import com.douunderstandapi.comment.domain.Comment;

public record CommentDeleteResponse(
        Long commentId
) {
    public static CommentDeleteResponse from(Comment comment) {
        return new CommentDeleteResponse(comment.getId());
    }
}
