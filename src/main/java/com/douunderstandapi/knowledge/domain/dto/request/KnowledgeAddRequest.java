package com.douunderstandapi.knowledge.domain.dto.request;

import com.douunderstandapi.knowledge.domain.Knowledge;
import com.douunderstandapi.user.domain.User;

public record KnowledgeAddRequest(
        String title,
        String content,
        String link,
        //TODO email -> JWT 도입시 삭제
        String email
) {
    public Knowledge toEntity(User user) {
        return Knowledge.of(
                this.title,
                this.content,
                this.link,
                user);
    }
}
