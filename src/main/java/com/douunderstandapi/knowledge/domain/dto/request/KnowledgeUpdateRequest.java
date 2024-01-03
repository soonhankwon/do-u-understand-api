package com.douunderstandapi.knowledge.domain.dto.request;

public record KnowledgeUpdateRequest(
        String title,
        String content,
        String link
) {
}
