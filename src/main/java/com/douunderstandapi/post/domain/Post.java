package com.douunderstandapi.post.domain;

import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.post.dto.request.PostUpdateRequest;
import com.douunderstandapi.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "`post`")
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

    @Column(name = "is_understand", nullable = false)
    private Boolean isUnderstand;

    @Column(name = "is_subscribe", nullable = false)
    private Boolean isSubscribe;

    @Column(name = "notification_count", nullable = false)
    private Integer notificationCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime createdAt;

    private Post(String title, String content, String link, User user) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.isUnderstand = false;
        this.isSubscribe = false;
        this.notificationCount = 0;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public static Post of(String title, String content, String link, User user) {
        return new Post(title, content, link, user);
    }

    public void update(PostUpdateRequest request) {
        if (request == null) {
            return;
        }
        this.title = request.title();
        this.content = request.content();
        this.link = request.link();
    }

    public void updateSubscribeStatus(Boolean isSubscribe) {
        if (isSubscribe == null) {
            return;
        }
        this.isSubscribe = isSubscribe;
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
