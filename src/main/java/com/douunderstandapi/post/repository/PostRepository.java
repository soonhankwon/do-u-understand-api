package com.douunderstandapi.post.repository;

import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.user.domain.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserAndIsSubscribe(User user, Boolean isSubscribe);

    Page<Post> findAllByUser(User user, Pageable pageable);
}
