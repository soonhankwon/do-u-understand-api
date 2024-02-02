package com.douunderstandapi.post.repository;

import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.post.enumType.PostStatus;
import com.douunderstandapi.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUser(User user, Pageable pageable);

    Page<Post> findAllByPostStatus(PostStatus postStatus, Pageable pageable);

    Page<Post> findAllByUserAndCategory_Name(User user, String categoryName, Pageable pageable);

    @Query("SELECT s.post FROM Subscribe s WHERE s.user.email = :email ORDER BY s.post.notificationCount ASC ")
    Page<Post> findPostWithMinNotificationCount(String email, Pageable pageable);
}
