package com.douunderstandapi.knowledge.domain.dto.response;

import com.douunderstandapi.knowledge.domain.Knowledge;

public record KnowledgeUpdateResponse(
        Long id,
        String title,
        String content,
        String link,
        Boolean isUnderstand
) {
    public static KnowledgeUpdateResponse of(Long id, String title, String content, String link, Boolean isUnderstand) {
        return new KnowledgeUpdateResponse(id, title, content, link, isUnderstand);
    }

    public static KnowledgeUpdateResponse from(Knowledge knowledge) {
        return KnowledgeUpdateResponse.of(
                knowledge.getId(),
                knowledge.getTitle(),
                knowledge.getContent(),
                knowledge.getLink(),
                knowledge.getIsUnderstand()
        );
    }
}
