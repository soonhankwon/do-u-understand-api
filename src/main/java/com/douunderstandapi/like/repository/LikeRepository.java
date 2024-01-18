package com.douunderstandapi.like.repository;

import com.douunderstandapi.like.domain.Like;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.user.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Page<Like> findAllByUser(User user, Pageable pageable);

    Optional<Like> findByUserAndPost(User user, Post post);

    void deleteByPostAndUser(Post post, User user);
}
