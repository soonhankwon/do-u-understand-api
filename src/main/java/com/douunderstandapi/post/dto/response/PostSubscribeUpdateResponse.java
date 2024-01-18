package com.douunderstandapi.post.dto.response;

import com.douunderstandapi.post.domain.Post;

public record PostSubscribeUpdateResponse(
        Long id,
        String title,
        String content,
        String link,
        Boolean isUnderstand,
        Boolean isSubscribe
) {
    public static PostSubscribeUpdateResponse of(Long id, String title, String content, String link,
                                                 Boolean isUnderstand,
                                                 Boolean isSubscribe) {
        return new PostSubscribeUpdateResponse(id, title, content, link, isUnderstand, isSubscribe);
    }

    public static PostSubscribeUpdateResponse from(Post post) {
        return PostSubscribeUpdateResponse.of(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLink(),
                post.getIsUnderstand(),
                post.getIsSubscribe()
        );
    }
}
