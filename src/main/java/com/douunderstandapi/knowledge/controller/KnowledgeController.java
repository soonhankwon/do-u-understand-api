package com.douunderstandapi.knowledge.controller;

import com.douunderstandapi.common.security.impl.UserDetailsImpl;
import com.douunderstandapi.knowledge.domain.dto.request.KnowledgeAddRequest;
import com.douunderstandapi.knowledge.domain.dto.request.KnowledgeUpdateRequest;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeAddResponse;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeGetResponse;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeUpdateResponse;
import com.douunderstandapi.knowledge.service.KnowledgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    @PostMapping
    public ResponseEntity<KnowledgeAddResponse> addKnowledge(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @RequestBody KnowledgeAddRequest request) {
        String email = userDetails.getUsername();
        KnowledgeAddResponse response = knowledgeService.addKnowledge(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{knowledgeId}")
    public ResponseEntity<KnowledgeGetResponse> getKnowledge(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PathVariable Long knowledgeId) {
        String email = userDetails.getUsername();
        KnowledgeGetResponse response = knowledgeService.findKnowledge(email, knowledgeId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{knowledgeId}")
    public ResponseEntity<KnowledgeUpdateResponse> updateKnowledge(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                   @PathVariable Long knowledgeId,
                                                                   @RequestBody KnowledgeUpdateRequest request) {
        String email = userDetails.getUsername();
        KnowledgeUpdateResponse response = knowledgeService.update(email, knowledgeId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{knowledgeId}")
    public ResponseEntity<String> deleteKnowledge(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @PathVariable Long knowledgeId) {
        String email = userDetails.getUsername();
        String response = knowledgeService.delete(email, knowledgeId);
        return ResponseEntity.ok().body(response);
    }
}
