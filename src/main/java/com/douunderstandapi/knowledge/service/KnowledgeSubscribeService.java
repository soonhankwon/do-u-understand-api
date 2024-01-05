package com.douunderstandapi.knowledge.service;

import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.knowledge.domain.Knowledge;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeSubscribeUpdateResponse;
import com.douunderstandapi.knowledge.repository.KnowledgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class KnowledgeSubscribeService {

    private final KnowledgeRepository knowledgeRepository;

    public KnowledgeSubscribeUpdateResponse updateKnowledgeSubscribe(Long knowledgeId, Boolean isSubscribe) {
        Knowledge knowledge = knowledgeRepository
                .findById(knowledgeId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_KNOWLEDGE_ID));

        knowledge.updateSubscribeStatus(isSubscribe);
        return KnowledgeSubscribeUpdateResponse.from(knowledge);
    }
}
