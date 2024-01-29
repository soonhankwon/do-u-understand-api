package com.douunderstandapi.post.domain;

import com.douunderstandapi.category.domain.Category;
import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.post.dto.request.PostUpdateRequest;
import com.douunderstandapi.post.enumType.PostStatus;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "`post`", indexes = {
        @Index(name = "post_user_id_idx", columnList = "user_id"),
        @Index(name = "post_post_status_idx", columnList = "post_status"),
        @Index(name = "post_category_id_idx", columnList = "category_id")
})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "link")
    private String link;

    @Column(name = "post_status", nullable = false)
    private PostStatus postStatus;

    @Column(name = "notification_count", nullable = false)
    private Integer notificationCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private Post(String title, String content, String link, User user, Category category) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.notificationCount = 0;
        this.postStatus = PostStatus.GENERAL;
        this.user = user;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    public static Post of(String title, String content, String link, User user, Category category) {
        return new Post(title, content, link, user, category);
    }

    public void update(PostUpdateRequest request, Category category) {
        assert request != null;
        this.title = request.title();
        this.content = request.content();
        this.link = request.link();
        this.category = category;
    }

    public void increaseNotificationCount() {
        this.notificationCount++;
    }

    public void validateAccessAuth(User user) {
        if (this.user != user) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.NO_AUTH_ACCESS);
        }
    }
}
