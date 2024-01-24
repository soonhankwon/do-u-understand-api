package com.douunderstandapi.comment.dto.request;

import jakarta.validation.constraints.NotNull;

public record CommentDeleteRequest(
        @NotNull(message = "코멘트 ID는 null을 허용하지 않습니다.")
        Long commentId
) {
}
