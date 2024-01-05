package com.douunderstandapi.user.domain;

import com.douunderstandapi.common.converter.AES256ToStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Convert(converter = AES256ToStringConverter.class)
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "authenticated", nullable = false)
    private Boolean isAuthenticated;

    @Column(name = "allowed_notification", nullable = false)
    private Boolean isAllowedNotification;

    private User(String email, String password, Boolean isAllowedNotification) {
        this.email = email;
        this.password = password;
        this.isAuthenticated = false;
        this.isAllowedNotification = isAllowedNotification;
    }

    public static User of(String email, String password, Boolean isAllowedNotification) {
        return new User(email, password, isAllowedNotification);
    }
}
