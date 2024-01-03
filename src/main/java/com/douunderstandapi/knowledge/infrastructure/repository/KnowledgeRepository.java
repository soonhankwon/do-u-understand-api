package com.douunderstandapi.knowledge.infrastructure.repository;

import com.douunderstandapi.knowledge.domain.Knowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {
}
