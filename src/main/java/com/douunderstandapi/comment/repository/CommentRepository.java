package com.douunderstandapi.comment.repository;

import com.douunderstandapi.comment.domain.Comment;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    Optional<Comment> findByIdAndUser(Long id, User user);

    Long countAllByPost(Post post);
}
