package com.douunderstandapi.post.dto.response;

import com.douunderstandapi.post.domain.Post;

public record PostAddResponse(
        Long id,
        String title,
        String content,
        String link
) {
    public static PostAddResponse of(Long id, String title, String content, String link) {
        return new PostAddResponse(id, title, content, link);
    }

    public static PostAddResponse from(Post post) {
        return PostAddResponse.of(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLink()
        );
    }
}
