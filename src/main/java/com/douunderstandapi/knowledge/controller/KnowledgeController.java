package com.douunderstandapi.knowledge.controller;

import com.douunderstandapi.knowledge.domain.dto.request.KnowledgeAddRequest;
import com.douunderstandapi.knowledge.domain.dto.request.KnowledgeUpdateRequest;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeAddResponse;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeGetResponse;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeUpdateResponse;
import com.douunderstandapi.knowledge.service.KnowledgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    @PostMapping
    public ResponseEntity<KnowledgeAddResponse> addKnowledge(@RequestBody KnowledgeAddRequest request) {
        KnowledgeAddResponse response = knowledgeService.addKnowledge(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{knowledgeId}")
    public ResponseEntity<KnowledgeGetResponse> getKnowledge(@PathVariable Long knowledgeId) {
        KnowledgeGetResponse response = knowledgeService.findKnowledge(knowledgeId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{knowledgeId}")
    public ResponseEntity<KnowledgeUpdateResponse> updateKnowledge(@PathVariable Long knowledgeId,
                                                                   @RequestBody KnowledgeUpdateRequest request) {
        KnowledgeUpdateResponse response = knowledgeService.update(knowledgeId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{knowledgeId}")
    public ResponseEntity<String> deleteKnowledge(@PathVariable Long knowledgeId) {
        String response = knowledgeService.delete(knowledgeId);
        return ResponseEntity.ok().body(response);
    }
}
