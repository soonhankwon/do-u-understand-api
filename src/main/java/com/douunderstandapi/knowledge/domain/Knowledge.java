package com.douunderstandapi.knowledge.domain;

import com.douunderstandapi.knowledge.domain.dto.request.KnowledgeUpdateRequest;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "knowledge")
public class Knowledge {

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

    private Knowledge(String title, String content, String link, User user) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.isUnderstand = false;
        this.isSubscribe = false;
        this.notificationCount = 0;
        this.user = user;
    }

    public static Knowledge of(String title, String content, String link, User user) {
        return new Knowledge(title, content, link, user);
    }

    public void update(KnowledgeUpdateRequest request) {
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
}
