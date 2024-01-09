package com.douunderstandapi.knowledge.controller;

import com.douunderstandapi.common.security.impl.UserDetailsImpl;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeSubscribeUpdateResponse;
import com.douunderstandapi.knowledge.service.KnowledgeSubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/knowledge")
public class KnowledgeSubscribeController {

    private final KnowledgeSubscribeService knowledgeSubscribeService;

    @PatchMapping("/{knowledgeId}")
    public ResponseEntity<KnowledgeSubscribeUpdateResponse> updateKnowledgeSubscribe(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long knowledgeId,
            @RequestParam Boolean isSubscribe) {
        String email = userDetails.getUsername();
        KnowledgeSubscribeUpdateResponse response = knowledgeSubscribeService.updateKnowledgeSubscribe(
                email, knowledgeId, isSubscribe);
        return ResponseEntity.ok().body(response);
    }
}
