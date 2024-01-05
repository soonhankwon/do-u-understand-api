package com.douunderstandapi.knowledge.repository;

import com.douunderstandapi.knowledge.domain.Knowledge;
import com.douunderstandapi.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {
    List<Knowledge> findAllByUserAndIsSubscribe(User user, Boolean isSubscribe);
}
