package com.douunderstandapi.knowledge.application;

import com.douunderstandapi.knowledge.infrastructure.repository.KnowledgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class KnowledgeService {

    private final KnowledgeRepository knowledgeRepository;

    public String addKnowledge() {
        return "added";
    }
}
