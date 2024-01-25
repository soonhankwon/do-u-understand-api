package com.douunderstandapi.comment.repository;

import com.douunderstandapi.comment.domain.Comment;
import com.douunderstandapi.post.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    Long countAllByPost(Post post);
}
