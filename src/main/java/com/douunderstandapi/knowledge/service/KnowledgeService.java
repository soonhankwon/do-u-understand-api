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

    public KnowledgeAddResponse addKnowledge(String email, KnowledgeAddRequest request) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Knowledge knowledge = request.toEntity(user);
        knowledgeRepository.save(knowledge);

        return KnowledgeAddResponse.from(knowledge);
    }

    @Transactional(readOnly = true)
    public KnowledgeGetResponse findKnowledge(String email, Long knowledgeId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Knowledge knowledge = knowledgeRepository
                .findById(knowledgeId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_KNOWLEDGE_ID));

        // 자신이 작성한 지식이 아니라면 조회 거부
        knowledge.validateAccessAuth(user);

        return KnowledgeGetResponse.from(knowledge);
    }

    public KnowledgeUpdateResponse update(String email, Long knowledgeId, KnowledgeUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Knowledge knowledge = knowledgeRepository
                .findById(knowledgeId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_KNOWLEDGE_ID));

        // 자신이 작성한 지식이 아니라면 업데이트 거부
        knowledge.validateAccessAuth(user);
        knowledge.update(request);
        return KnowledgeUpdateResponse.from(knowledge);
    }

    public String delete(String email, Long knowledgeId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Knowledge knowledge = knowledgeRepository
                .findById(knowledgeId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_KNOWLEDGE_ID));

        // 자신이 작성한 지식이 아니라면 삭제 거부
        knowledge.validateAccessAuth(user);
        knowledgeRepository.delete(knowledge);

        return "deleted";
    }
}
