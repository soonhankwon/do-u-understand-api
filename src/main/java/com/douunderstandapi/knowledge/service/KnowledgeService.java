package com.douunderstandapi.knowledge.service;

import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.knowledge.domain.Knowledge;
import com.douunderstandapi.knowledge.domain.dto.request.KnowledgeAddRequest;
import com.douunderstandapi.knowledge.domain.dto.request.KnowledgeUpdateRequest;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeAddResponse;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeGetResponse;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeUpdateResponse;
import com.douunderstandapi.knowledge.repository.KnowledgeRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class KnowledgeService {

    private final KnowledgeRepository knowledgeRepository;
    private final UserRepository userRepository;

    public KnowledgeAddResponse addKnowledge(KnowledgeAddRequest request) {
        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Knowledge knowledge = request.toEntity(user);
        knowledgeRepository.save(knowledge);

        return KnowledgeAddResponse.from(knowledge);
    }

    @Transactional(readOnly = true)
    public KnowledgeGetResponse findKnowledge(Long knowledgeId) {
        Knowledge knowledge = knowledgeRepository
                .findById(knowledgeId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_KNOWLEDGE_ID));

        return KnowledgeGetResponse.from(knowledge);
    }

    public KnowledgeUpdateResponse update(Long knowledgeId, KnowledgeUpdateRequest request) {
        Knowledge knowledge = knowledgeRepository
                .findById(knowledgeId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_KNOWLEDGE_ID));

        knowledge.update(request);
        return KnowledgeUpdateResponse.from(knowledge);
    }

    public String delete(Long knowledgeId) {
        Knowledge knowledge = knowledgeRepository
                .findById(knowledgeId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_KNOWLEDGE_ID));

        knowledgeRepository.delete(knowledge);
        return "deleted";
    }
}
