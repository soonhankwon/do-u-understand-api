package com.douunderstandapi.knowledge.domain.dto.response;

import com.douunderstandapi.knowledge.domain.Knowledge;

public record KnowledgeSubscribeUpdateResponse(
        Long id,
        String title,
        String content,
        String link,
        Boolean isUnderstand,
        Boolean isSubscribe
) {
    public static KnowledgeSubscribeUpdateResponse of(Long id, String title, String content, String link,
                                                      Boolean isUnderstand,
                                                      Boolean isSubscribe) {
        return new KnowledgeSubscribeUpdateResponse(id, title, content, link, isUnderstand, isSubscribe);
    }

    public static KnowledgeSubscribeUpdateResponse from(Knowledge knowledge) {
        return KnowledgeSubscribeUpdateResponse.of(
                knowledge.getId(),
                knowledge.getTitle(),
                knowledge.getContent(),
                knowledge.getLink(),
                knowledge.getIsUnderstand(),
                knowledge.getIsSubscribe()
        );
    }
}
