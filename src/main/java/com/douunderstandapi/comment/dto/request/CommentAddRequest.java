package com.douunderstandapi.comment.dto.request;

import com.douunderstandapi.comment.domain.Comment;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.user.domain.User;

public record CommentAddRequest(
        Long postId,
        String content
) {
    public Comment toEntity(CommentAddRequest request, User user, Post post) {
        return new Comment(request.content, user, post);
    }
}
