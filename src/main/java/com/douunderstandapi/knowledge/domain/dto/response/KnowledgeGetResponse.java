package com.douunderstandapi.knowledge.domain.dto.response;

import com.douunderstandapi.knowledge.domain.Knowledge;

public record KnowledgeGetResponse(
        Long id,
        String title,
        String content,
        String link,
        Boolean isUnderstand
) {
    public static KnowledgeGetResponse of(Long id, String title, String content, String link, Boolean isUnderstand) {
        return new KnowledgeGetResponse(id, title, content, link, isUnderstand);
    }

    public static KnowledgeGetResponse from(Knowledge knowledge) {
        return KnowledgeGetResponse.of(
                knowledge.getId(),
                knowledge.getTitle(),
                knowledge.getContent(),
                knowledge.getLink(),
                knowledge.getIsUnderstand()
        );
    }
}
