package com.douunderstandapi.post.dto.request;

import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.user.domain.User;

public record PostAddRequest(
        String title,
        String content,
        String link
) {
    public Post toEntity(User user) {
        return Post.of(
                this.title,
                this.content,
                this.link,
                user);
    }
}
