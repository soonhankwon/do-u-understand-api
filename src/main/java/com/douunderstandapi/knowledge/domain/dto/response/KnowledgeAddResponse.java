package com.douunderstandapi.knowledge.domain.dto.response;

import com.douunderstandapi.knowledge.domain.Knowledge;

public record KnowledgeAddResponse(
        Long id,
        String title,
        String content,
        String link,
        Boolean isUnderstand
) {
    public static KnowledgeAddResponse of(Long id, String title, String content, String link, Boolean isUnderstand) {
        return new KnowledgeAddResponse(id, title, content, link, isUnderstand);
    }

    public static KnowledgeAddResponse from(Knowledge knowledge) {
        return KnowledgeAddResponse.of(
                knowledge.getId(),
                knowledge.getTitle(),
                knowledge.getContent(),
                knowledge.getLink(),
                knowledge.getIsUnderstand()
        );
    }
}
