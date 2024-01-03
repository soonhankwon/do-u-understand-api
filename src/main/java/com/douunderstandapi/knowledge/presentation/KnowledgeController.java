package com.douunderstandapi.knowledge.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/knowledge")
public class KnowledgeController {

    @PostMapping
    public ResponseEntity<?> addKnowledge() {
        return ResponseEntity.status(HttpStatus.CREATED).body("added");
    }
}
