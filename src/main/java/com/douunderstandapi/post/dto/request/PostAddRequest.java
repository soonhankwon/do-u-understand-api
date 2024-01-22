package com.douunderstandapi.post.dto.request;

import com.douunderstandapi.category.domain.Category;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.user.domain.User;
import java.util.List;

public record PostAddRequest(
        String title,
        String content,
        String link,
        List<String> categoriesName
) {
    public Post toEntity(User user, Category category) {
        return Post.of(
                this.title,
                this.content,
                this.link,
                user,
                category);
    }
}
