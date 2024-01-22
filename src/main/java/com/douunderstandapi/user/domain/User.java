package com.douunderstandapi.user.domain;

import com.douunderstandapi.common.converter.AES256ToStringConverter;
import com.douunderstandapi.common.entity.UserBaseTimeEntity;
import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.user.enumType.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "`user`")
public class User extends UserBaseTimeEntity {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus;

    private User(String email, String password, Boolean isAllowedNotification) {
        this.email = email;
        this.password = password;
        this.isAuthenticated = true;
        this.isAllowedNotification = isAllowedNotification;
        this.userStatus = UserStatus.ACTIVE;
    }

    public static User of(String email, String password, Boolean isAllowedNotification) {
        return new User(email, password, isAllowedNotification);
    }

    public void delete() {
        this.userStatus = UserStatus.DELETED;
        setDeletedAtNow();
    }

    public void validatePassword(String password, BiFunction<String, String, Boolean> matches) {
        if (!matches.apply(password, this.password)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_MATCHED_EMAIL_AND_PASSWORD);
        }
    }

    public void updatePassword(String password, Function<String, String> encodeFunction) {
        this.password = encodeFunction.apply(password);
    }

    public void validateStatus() {
        if (this.userStatus == UserStatus.DELETED) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.DELETED_USER);
        }
    }
}
