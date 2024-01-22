package com.douunderstandapi.post.dto.response;

import com.douunderstandapi.post.domain.Post;

public record PostAddResponse(
        Long id,
        String userEmail,
        String title,
        String content,
        String link,
        int commentCount,
        String categoryName
) {
    public static PostAddResponse of(Long id, String userEmail, String title, String content, String link,
                                     String category) {
        return new PostAddResponse(id, userEmail, title, content, link, 0, category);
    }

    public static PostAddResponse from(Post post) {
        return PostAddResponse.of(
                post.getId(),
                post.getUser().getEmail(),
                post.getTitle(),
                post.getContent(),
                post.getLink(),
                post.getCategory().getName()
        );
    }
}
