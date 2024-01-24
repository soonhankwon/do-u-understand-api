package com.douunderstandapi.comment.dto.request;

import com.douunderstandapi.comment.domain.Comment;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentAddRequest(

        @NotNull(message = "포스트 ID는 null을 허용하지 않습니다.")
        Long postId,

        @NotBlank(message = "컨텐츠는 null 또는 공백문자를 허용하지 않습니다.")
        String content
) {
    public Comment toEntity(CommentAddRequest request, User user, Post post) {
        return new Comment(request.content, user, post);
    }
}
