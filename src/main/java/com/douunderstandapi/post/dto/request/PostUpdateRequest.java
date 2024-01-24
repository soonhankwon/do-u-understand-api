package com.douunderstandapi.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostUpdateRequest(
        @NotBlank(message = "제목은 null 또는 공백문자를 허용하지 않습니다.")
        String title,

        @NotBlank(message = "컨텐츠는 null 또는 공백문자를 허용하지 않습니다.")
        String content,

        @NotNull(message = "링크는 null을 허용하지 않습니다.")
        String link,

        @NotBlank(message = "카테고리는 null 또는 공백문자를 허용하지 않습니다.")
        String categoryName
) {
}
