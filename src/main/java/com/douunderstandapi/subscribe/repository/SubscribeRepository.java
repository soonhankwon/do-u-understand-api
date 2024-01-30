package com.douunderstandapi.subscribe.repository;

import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.subscribe.domain.Subscribe;
import com.douunderstandapi.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
    List<Subscribe> findAllByUser(User user);

    Page<Subscribe> findAllByUser(User user, Pageable pageable);

    Optional<Subscribe> findByUserAndPost(User user, Post post);

    void deleteByPostAndUser(Post post, User user);

    void deleteAllByPost(Post post);

    List<Subscribe> findAllByPost(Post post);

}
