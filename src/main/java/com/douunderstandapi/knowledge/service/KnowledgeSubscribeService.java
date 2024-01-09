package com.douunderstandapi.knowledge.service;

import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.knowledge.domain.Knowledge;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeSubscribeUpdateResponse;
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
public class KnowledgeSubscribeService {

    private final KnowledgeRepository knowledgeRepository;
    private final UserRepository userRepository;

    public KnowledgeSubscribeUpdateResponse updateKnowledgeSubscribe(String email, Long knowledgeId,
                                                                     Boolean isSubscribe) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Knowledge knowledge = knowledgeRepository
                .findById(knowledgeId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_KNOWLEDGE_ID));

        // 자신이 작성한 지식이 아니라면 구독 거부
        knowledge.validateAccessAuth(user);
        knowledge.updateSubscribeStatus(isSubscribe);
        return KnowledgeSubscribeUpdateResponse.from(knowledge);
    }
}
