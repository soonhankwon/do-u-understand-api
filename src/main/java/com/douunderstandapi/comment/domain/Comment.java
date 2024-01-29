package com.douunderstandapi.comment.domain;

import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`comment`", indexes = {
        @Index(name = "comment_user_id_idx", columnList = "user_id"),
        @Index(name = "comment_post_id_idx", columnList = "post_id")
})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(String content, User user, Post post) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.user = user;
        this.post = post;
    }

    public void validateDeleteAuth(User user) {
        if (this.user != user) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.INVALID_ACCESS);
        }
    }
}
